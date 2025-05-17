package com.test.sandbox;

import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.List;


@Slf4j
@Configuration
@EnableIntegration
public class JmsIntegrationConfig {


    private String queueName="queue";

    @Primary
    @Bean
    public LiquibaseProperties liquibaseProperties() {
        LiquibaseProperties properties = new LiquibaseProperties();
        properties.setEnabled(false); // Disable Liquibase programmatically
        return properties;
    }


    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    @Bean("jmsAmqConnectionFactory")
    public ConnectionFactory getConnectionFactory() {
        log.info("***** ☞ flow JMS Active MQ connection factory done to {}", "");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("localhost", "ee", "ee");
        connectionFactory.setTrustedPackages(List.of("java"));
        return connectionFactory;
    }


    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow jmsInboundFlow(ConnectionFactory connectionFactory) {
        return IntegrationFlow
                .from(org.springframework.integration.jms.dsl.Jms.messageDrivenChannelAdapter(connectionFactory)
                        .destination(queueName)
                        .configureListenerContainer(jmsDefaultListenerContainerSpec ->
                        jmsDefaultListenerContainerSpec.sessionTransacted(true) )// Example - Enable

                                .retryTemplate(retryTemplate()))
                .channel(inputChannel())
                .handle(message -> {
                    System.out.println("Received JMS message: " + message.getPayload());
                })
                .get();
    }
}
