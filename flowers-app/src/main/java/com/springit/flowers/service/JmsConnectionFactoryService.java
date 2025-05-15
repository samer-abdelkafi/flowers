package com.springit.flowers.service;


import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.springit.flowers.entity.Destination;
import com.springit.flowers.entity.JmsDestination;
import com.springit.flowers.model.AmqServer;
import com.springit.flowers.model.MQServer;
import com.springit.flowers.model.Server;
import com.springit.flowers.model.ServerBuilder;
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

import static com.springit.flowers.model.Server.ServerType.AMQ;


@Slf4j
@Service
@AllArgsConstructor
public class JmsConnectionFactoryService {


    private final ServerService serverService;

    @Cacheable("jmsConnectionFactoryCash")
    public ConnectionFactory getConnectionFactory(String serverName) {

        log.info("***** ☞ flow JMS Active MQ connection factory for server {}", serverName);
        Server server = this.serverService.getServer(serverName);
        Server.ServerType serverType = server.getType();

        return switch (serverType) {
            case AMQ -> getActiveMQConnectionFactory((AmqServer) server);
            case MQ -> getMqConnectionFactory((MQServer) server);
            default -> throw new RuntimeException("Failed to get connection factory for Jms server" + server);
        };
    }


    public ConnectionFactory getConnectionFactory(Destination destination) {
        return Optional.of(destination)
                .map(JmsDestination.class::cast)
                .map(JmsDestination::getServerName)
                .map(this::getConnectionFactory)
                .orElseThrow(() -> new RuntimeException("failed to get connection factory for destination "
                        + destination.getDescription()));
    }

    @SneakyThrows
    private ConnectionFactory getMqConnectionFactory(MQServer server) {

        MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
        mqConnectionFactory.setHostName(server.getHost());
//        mqConnectionFactory.setPort(server.getPort());
//        mqConnectionFactory.setChannel(server.getChannel());
        mqConnectionFactory.setTransportType(1);
        return mqConnectionFactory;
    }

    private ConnectionFactory getActiveMQConnectionFactory(AmqServer server) {
        String jmsFlowUser = server.getUser();
        String jmsFlowPassword = server.getPassword();
        String jmsFlowUrl = server.getHost();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsFlowUser, jmsFlowPassword, jmsFlowUrl);
        connectionFactory.setTrustedPackages(List.of("java"));
        return connectionFactory;
    }


}
