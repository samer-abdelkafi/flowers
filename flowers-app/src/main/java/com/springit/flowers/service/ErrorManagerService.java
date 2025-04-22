package com.springit.flowers.service;

import com.springit.flowers.entity.ErrorEntity;
import com.springit.flowers.entity.ErrorEntityId;
import com.springit.flowers.entity.JmsDestination;
import com.springit.flowers.repo.DestinationRepo;
import com.springit.flowers.repo.ErrorEntityRepo;
import com.springit.flowers.util.ErrorStatus;
import jakarta.jms.ConnectionFactory;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import static com.springit.flowers.service.FlowService.DESTINATION_ID;


@Slf4j
@Component
@AllArgsConstructor
public class ErrorManagerService {


    public static final String FOLDER_MESSAGE_PATH = "FOLDER_MESSAGE_PATH";
    public static final String JMS_MESSAGE_ID = "jms_messageId";
    public static final String ERROR_MANAGEMENT_ERROR_CHANNEL = "errorManagementErrorChannel";
    public static final String RETRY_MESSAGE_CHANEL = "retryMessageChanel";

    private final ConnectionFactory jmsConnectionFactory;

    private final JmsConnectionFactoryService jmsConnectionFactoryService;

    private final FlowRegistrationService flowRegistrationService;

    private final DestinationRepo destinationRepo;

    private final ErrorEntityRepo errorEntityRepo;

    private final StorageService storageService;

    private final EntityManagerFactory entityManagerFactory;

   // @EventListener
    public void createErrorManagementFlows(ContextRefreshedEvent ignoredEvent) {

        IntegrationFlow errorManagementErrorFlow = IntegrationFlow.from(ERROR_MANAGEMENT_ERROR_CHANNEL)
                .handle(message -> updateErrorDbStatus(message, ErrorStatus.ERROR))
                .get();
        flowRegistrationService.registerFlow("errorManagementErrorFlow", errorManagementErrorFlow);

        IntegrationFlow errorManagementFlow = IntegrationFlow
                .from(Jms.inboundAdapter(jmsConnectionFactory).destination("FlowErrorQueue"))
                .enrichHeaders(h -> h.errorChannel(ERROR_MANAGEMENT_ERROR_CHANNEL, true))
                .handle(this::saveErrorToDb)
                .channel(RETRY_MESSAGE_CHANEL)
                .handle(this::readFileFromStorage)
                .handle(this::retrySendJms)
                .handle(message -> updateErrorDbStatus(message, ErrorStatus.OK))
                .get();

        flowRegistrationService.registerFlow("errorManagementFlow", errorManagementFlow);

        IntegrationFlow errorRetryFlow = IntegrationFlow
                .from(Jpa.inboundAdapter(entityManagerFactory)
                                .entityClass(ErrorEntity.class)
                                .jpaQuery("from ErrorEntity ee where ee.status = 'ERROR'")
                                .maxResults(100),
                        e -> e.poller(p -> p.fixedRate(10000)))  // Poll every 1 hour
                .split()
                .handle((body, headers) -> {
                    ErrorEntity errorEntity = (ErrorEntity) body;
                    return MessageBuilder.withPayload(body)
                            .setHeader(FOLDER_MESSAGE_PATH, errorEntity.getFilePath())
                            .setHeader(DESTINATION_ID, errorEntity.getErrorEntityId().getDestinationId())
                            .setHeader(FlowService.FLOW_ID, errorEntity.getErrorEntityId().getFlowId())
                            .setHeader(JMS_MESSAGE_ID, errorEntity.getErrorEntityId().getJmsMessageId())
                            .build();
                })
                .handle((body, headers) -> {
                    updateErrorDbStatus(MessageBuilder.createMessage(body, headers), ErrorStatus.RETRYING);
                    return body;
                })
                .enrichHeaders(h -> h.errorChannel(ERROR_MANAGEMENT_ERROR_CHANNEL, true))
                .channel(RETRY_MESSAGE_CHANEL)
                .get();

        flowRegistrationService.registerFlow("errorRetryFlow", errorRetryFlow);

    }

    private Object readFileFromStorage(Object payload, MessageHeaders headers) {

        return Optional.ofNullable(headers.get(FOLDER_MESSAGE_PATH, String.class))
                .map(storageService::readMessage)
                .orElseThrow(() -> new RuntimeException("failed to get file from S3"));
    }

    private void updateErrorDbStatus(Message<?> message, ErrorStatus status) {
        MessageHeaders headers = getMessageHeaders(message);
        int flowId = headers.get(FlowService.FLOW_ID, Integer.class);
        int destinationId = headers.get(DESTINATION_ID, Integer.class);
        String jmsMessageId = headers.get(JMS_MESSAGE_ID, String.class);
        Date okDate = ErrorStatus.OK.equals(status) ? new Date() : null;
//        int rowUpdated = errorEntityRepo.updateStateById(flowId, destinationId, jmsMessageId, status, okDate);
//        if (rowUpdated == 0) {
//            log.error("No row updated for jms message {}", jmsMessageId);
//        }
    }

    private static MessageHeaders getMessageHeaders(Message<?> message) {
        return Optional.of(message)
                .filter(m -> m instanceof ErrorMessage)
                .map(ErrorMessage.class::cast)
                .map(ErrorMessage::getPayload)
                .filter(errorMessage -> errorMessage instanceof  MessageHandlingException)
                .map(MessageHandlingException.class::cast)
                .map(MessageHandlingException::getFailedMessage)
                .map(Message::getHeaders)
                .orElse(message.getHeaders());
    }

    // assuming constants and imports are set correctly
    public Object retrySendJms(Object payload, MessageHeaders headers) {
        // Assuming DESTINATION_ID is a constant defined elsewhere:
        Integer id = headers.get(DESTINATION_ID, Integer.class);
        if (id == null) {
            throw new IllegalArgumentException("Destination ID must not be null");
        }

        // Fetch the destination from a repository (assumed method and error handling)
        JmsDestination destination = (JmsDestination) destinationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Failed to get destination with id = " + id));

        // Get ConnectionFactory from a service (assumed method)
        ConnectionFactory connectionFactory = jmsConnectionFactoryService.getConnectionFactory(destination);
        if (connectionFactory == null) {
            throw new RuntimeException("Failed to get ConnectionFactory for destination with id = " + id);
        }

        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setDefaultDestinationName(destination.getQueueName());

        // Correct the use of jmsTemplate.send() by using a MessageCreator
        jmsTemplate.send(session -> session.createObjectMessage((Serializable) payload));
        return payload;
    }

    private Object saveErrorToDb(Object payload, MessageHeaders headers) {
        ErrorEntity errorEntity = buildErrorEntity(payload, headers);
        errorEntityRepo.save(errorEntity);
        return payload;
    }


    private ErrorEntity buildErrorEntity(Object payload, MessageHeaders headers) {
        ErrorEntity errorEntity = new ErrorEntity();
        ErrorEntityId errorEntityId = new ErrorEntityId();
        errorEntityId.setFlowId(headers.get(FlowService.FLOW_ID, Integer.class));
        errorEntityId.setDestinationId(headers.get(DESTINATION_ID, Integer.class));
        errorEntityId.setJmsMessageId(headers.get(JMS_MESSAGE_ID, String.class));
//        errorEntity.setErrorEntityId(errorEntityId);
//        errorEntity.setErrorDate(new Date());
//        errorEntity.setFilePath(headers.get(FOLDER_MESSAGE_PATH, String.class));
//        errorEntity.setStatus(ErrorStatus.RETRYING);
//        errorEntity.setPayload(payload);
//        errorEntity.setBytesArray(payload instanceof BytesMessage);
//        errorEntity.setServer(System.getProperty("server.name"));
        return errorEntity;
    }
}

