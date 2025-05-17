package com.springit.flowers.model;

import lombok.Data;

@Data
public class ServerBuilder {
    
        private String name;
    
        private Server.ServerType type;
    
        private String host;
    
        private String user;
    
        private String password;

    
        public Server build() {
            Server server;
            switch (type) {
                case MQ -> server = new MQServer(this.name, this.type, this.host, this.user, this.password);
                case AMQ -> server = new AmqServer(this.name, this.type, this.host, this.user, this.password);
                case KAFKA -> server = new KafkaServer(this.name, this.type, this.host, this.user, this.password);
                default -> throw new IllegalStateException("Unexpected value: " + type);
            }
            return server;
        }

    
        public String toString() {
            String var10000 = this.name;
            return "Server.ServerBuilder(name=" + var10000 + ", type=" + this.type + ", host=" + this.host + ", user=" + this.user + ", password=" + this.password + ")";
        }
    }