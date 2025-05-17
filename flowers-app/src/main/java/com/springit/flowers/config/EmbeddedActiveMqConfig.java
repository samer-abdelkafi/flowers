package com.springit.flowers.config;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class EmbeddedActiveMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedActiveMqConfig.class);

    @Bean
    public BrokerService broker() throws Exception {
        // Create and configure the embedded ActiveMQ broker
        BrokerService broker = new BrokerService();
        broker.setBrokerName("embedded-broker");
        broker.addConnector("tcp://localhost:61616"); // Open a TCP connector
        broker.setPersistent(false); // Use an in-memory broker
        broker.setUseJmx(true); // Enable JMX (optional)
        
        // Start the broker
        LOGGER.info("Starting embedded ActiveMQ broker...");
        return broker;
    }
}