package com.springit.flowers.model;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Data
public abstract class Server {

    private String name;
    private ServerType type;
    private String host;
    private String user;
    private String password;


    public static enum ServerType {
        AMQ,
        KAFKA,
        MQ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(name, server.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}