package com.springit.flowers.service;


import com.springit.flowers.entity.Destination;
import com.springit.flowers.entity.JmsDestination;
import com.springit.flowers.entity.Flow;
import com.springit.flowers.entity.FlowSubscriber;
import com.springit.flowers.filter.IAttributesExtractor;
import com.springit.flowers.filter.SubscriberMessageFilter;
import com.springit.flowers.util.JexlParserUtils;
import com.springit.flowers.util.StringUtils;
import jakarta.jms.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.HeaderEnricherSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PublishSubscribeSpec;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.springit.flowers.entity.AuditEntity.AuditEntityStatus.*;


@Slf4j
@Service
@AllArgsConstructor
public class FlowService {

    public static final String FILTERED_SUBSCRIBERS = "FILTERED_SUBSCRIBERS";
    public static final String DESTINATION_ID = "DESTINATION_ID";
    public static final String FLOW_ID = "FLOW_ID";

    private final JmsConnectionFactoryService jmsConnectionFactoryService;

    private final FlowRegistrationService flowRegistrationService;

    private final FlowRepoService flowRepoService;

    private final AuditService auditService;

    private final StorageService storageService;

    private final ConnectionFactory flowsConnectionFactory;


    @EventListener
    public void createAllFlows(ContextRefreshedEvent ignoredEvent) {
        errorHandlingFlow();
        flowRepoService.getAll()
                .forEach(this::createFlow);
    }



    public void errorHandlingFlow() {
        IntegrationFlow flow =  IntegrationFlow.from("flowsErrorChannel")
                .handle((payload, headers) -> {
                    MessageHandlingException messageHandlingException = (MessageHandlingException) payload;
                    Message<?> failedMessage = messageHandlingException.getFailedMessage();

                    // Audit and Store as before
                    auditService.audit(messageHandlingException, failedMessage.getHeaders(), ERROR);
                    storageService.writeMessage(failedMessage.getPayload(), failedMessage.getHeaders(), StorageService.TypeMessage.error);

                    // Create new headers map with additional error message header
                    // add new header

                    return MessageBuilder.fromMessage(failedMessage)
                            .copyHeadersIfAbsent(failedMessage.getHeaders())
                            .setHeader("ERROR_MESSAGE", messageHandlingException.getCause().getMessage())  // add new header
                            .build();
                })
                .handle(Jms.outboundAdapter(flowsConnectionFactory).destination("FlowErrorQueue"))
                .get();

        this.flowRegistrationService.registerFlow("ErrorHandler", flow);
    }

    public void createFlow(Flow flow) {

        try {
            String commonInputChannelName = flow.getId() + "_CommonInputChannel";

            flow.getPublishers().forEach(publisher -> {
                IntegrationFlow inboundAdapterFlow = createInboundAdapterFlow(publisher, commonInputChannelName);
                this.flowRegistrationService.registerFlow(flow.getId() + "_IntegrationFlow_Pub_" + publisher.getId(), inboundAdapterFlow);
            });

            IntegrationFlow integrationFlow = setupCommonInputChannelFlow(flow, commonInputChannelName);
            this.flowRegistrationService.registerFlow(flow.getId() + "_IntegrationFlow", integrationFlow);

        } catch (Throwable e) {
            log.error("Failed to start Flow ID = {} name = {} !!!", flow.getId(), flow.getName(), e);
        }
    }


    private IntegrationFlow createInboundAdapterFlow(Destination destination, String inputChannel) {
        String queueName = getQueueName(destination);
        ConnectionFactory connectionFactory = jmsConnectionFactoryService.getConnectionFactory(destination);
        return IntegrationFlow.from(Jms.inboundAdapter(connectionFactory).destination(queueName))
                .handle((payload, headers) -> storageService.writeMessage(payload, headers, StorageService.TypeMessage.input))
                .channel(inputChannel)  // Directs messages to the common channel
                .get();
    }


    protected static void enrichHeader(HeaderEnricherSpec enricherSpec, Flow flow) {
        enricherSpec.headerFunction(FILTERED_SUBSCRIBERS, message -> getFilteredSubscribers(message, flow));
        enricherSpec.header(FLOW_ID, flow.getId());
    }

    private static Set<Integer> getFilteredSubscribers(Message<Object> message, Flow flow) {
        Set<String> filters = StringUtils.getSetFromStringCsv(flow.getFilterTypesStringFormat());
        Set<String> attributes = StringUtils.getSetFromStringCsv(flow.getAttributesStringFormat());
        Map<String, Object> params = getAttributesValue(filters, attributes, message);

        return flow.getSubscribers().stream()
                .filter(subscriber -> JexlParserUtils.evaluate(subscriber.getCondition(), params))
                .map(FlowSubscriber::getDestination)
                .map(Destination::getId)
                .collect(Collectors.toSet());
    }


    protected static Map<String, Object> getAttributesValue(Set<String> extractors, Set<String> attributes,
                                                            Message<?> message) {
        return extractors.stream()
                .map(extractor -> getAttributeValue(extractor, attributes, message))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> newValue // In case of a key collision, keep the latest value
                ));
    }

    @SneakyThrows
    protected static Map<String, Object> getAttributeValue(String extractor, Set<String> attributes, Message<?> message) {
        Class<?> rawClass = Class.forName("com.springit.flowers.filter.impl." + extractor);
        Class<? extends IAttributesExtractor> attributesExtractorClass = rawClass.asSubclass(IAttributesExtractor.class);
        IAttributesExtractor attributesExtractor = attributesExtractorClass.getDeclaredConstructor().newInstance();
        return attributesExtractor.extractAttributes(attributes, message);
    }

    private IntegrationFlow setupCommonInputChannelFlow(Flow flow, String inputChannel) {
        return IntegrationFlow.from(inputChannel) // All inbound adapters write to this channel
                .enrichHeaders(enricherSpec -> enrichHeader(enricherSpec, flow))
                .publishSubscribeChannel(Executors.newCachedThreadPool(),
                        spec -> flow.getSubscribers().forEach(subscriber -> createOutboundAdapter(spec, subscriber)))
                .get();
    }

    private void createOutboundAdapter(PublishSubscribeSpec spec, FlowSubscriber subscriber) {
        spec.subscribe(flow -> flow
                .filter(new SubscriberMessageFilter(subscriber.getDestination().getId())) // Apply the subscriber's condition
                .enrichHeaders(headerEnricherSpec -> headerEnricherSpec
                        .header(DESTINATION_ID, subscriber.getDestination().getId())
                        .header("SUBSCRIBER_SERVER", ((JmsDestination) subscriber.getDestination()).getServerName())
                )
                .handle((payload, headers) -> auditService.audit(payload, headers, PENDING))
                .channel(subscriber.getDestination().getId() + "_channelFilterHandler")
                .enrichHeaders(h -> h.errorChannel("flowsErrorChannel"))
                .wireTap(wFlow -> wFlow.handle(Jms.outboundAdapter(jmsConnectionFactoryService.getConnectionFactory(subscriber.getDestination()))
                        .destination(getSubscriberDestination(subscriber))
                ))
                .handle((payload, headers) -> {
                    auditService.audit(payload, headers, OK);
                    return null;
                })// Handle message to JMS destination
        );  // End the flow for each subscriber here
    }



    private static String getQueueName(Destination destination) {
        return Optional.of(destination).map(JmsDestination.class::cast)
                .map(JmsDestination::getQueueName)
                .orElseThrow(() -> new RuntimeException("Error to get publisher Queue of destination id " + destination.getId() + " !!!"));
    }

    private static String getSubscriberDestination(FlowSubscriber subscriber) {
        return Optional.of(subscriber)
                .map(FlowSubscriber::getDestination)
                .map(JmsDestination.class::cast)
                .map(JmsDestination::getQueueName)
                .orElseThrow(() -> new RuntimeException("Error to get subscriber Queue!!!"));
    }

}
