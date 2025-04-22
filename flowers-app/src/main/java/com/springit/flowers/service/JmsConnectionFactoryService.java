package com.springit.flowers.service;


import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.springit.flowers.entity.Destination;
import com.springit.flowers.entity.JmsDestination;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Service;

import jakarta.jms.ConnectionFactory;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class JmsConnectionFactoryService {


    private final Environment environment;

    @Cacheable("jmsConnectionFactoryCash")
    public ConnectionFactory getConnectionFactory(String server) {

        log.info("***** ☞ flow JMS Active MQ connection factory for server {}", server);
        String jmsFlowType = this.environment.getRequiredProperty(String.format("jms.%s.type", server));

        return switch (jmsFlowType) {
            case "AMQ" -> getActiveMQConnectionFactory(server);
            case "MQS" -> getMqConnectionFactory(server);
            default -> throw new RuntimeException("Failed to get connection factory for Jms server" + server);
        };
    }


    public ConnectionFactory getConnectionFactory(Destination destination) {
        return Optional.of(destination)
                .map(JmsDestination.class::cast)
                .map(JmsDestination::getConfigName)
                .map(this::getConnectionFactory)
                .orElseThrow(() -> new RuntimeException("failed to get connection factory for destination "
                        + destination.getDescription()));
    }

    @SneakyThrows
    private ConnectionFactory getMqConnectionFactory(String server) {
        String channel = this.environment.getRequiredProperty(String.format("jms.%s.channel", server));
        Integer port = this.environment.getRequiredProperty(String.format("jms.%s.port", server), Integer.class);
        String hostName = this.environment.getRequiredProperty(String.format("jms.%s.host", server));

        MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
        mqConnectionFactory.setHostName(hostName);
        mqConnectionFactory.setPort(port);
        mqConnectionFactory.setChannel(channel);
        mqConnectionFactory.setTransportType(1);
        return mqConnectionFactory;
    }

    private ConnectionFactory getActiveMQConnectionFactory(String server) {
        String jmsFlowUser = this.environment.getProperty(String.format("jms.%s.user", server));
        String jmsFlowPassword = this.environment.getProperty(String.format("jms.%s.password", server));
        String jmsFlowUrl = this.environment.getRequiredProperty(String.format("jms.%s.url", server));
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsFlowUser, jmsFlowPassword, jmsFlowUrl);
        connectionFactory.setTrustedPackages(List.of("java"));
        return connectionFactory;
    }


}
