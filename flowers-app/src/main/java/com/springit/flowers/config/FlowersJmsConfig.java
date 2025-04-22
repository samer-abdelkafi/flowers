package com.springit.flowers.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import jakarta.jms.ConnectionFactory;
import java.util.List;

@Configuration
@Primary
@Slf4j
public class FlowersJmsConfig {

    @Value("${flow.jms.url}")
    private String jmsFlowUrl;

    @Value("${flow.jms.user}")
    private String jmsFlowUser;

    @Value("${flow.jms.pwd}")
    private String jmsFlowPassword;

//    @Bean("jmsAmqConnectionFactory")
//    public ConnectionFactory getConnectionFactory() {
//        log.info("***** ☞ flow JMS Active MQ connection factory done to {}", jmsFlowUrl);
//        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsFlowUser, jmsFlowPassword, jmsFlowUrl);
//        connectionFactory.setTrustedPackages(List.of("java"));
//        return connectionFactory;
//    }


}
