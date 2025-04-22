package com.springit.flowers.service;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class ServerService {

    private Environment environment;


    public Set<String> getAllServers() {

        // Assuming that the property name where servers are listed is "server.list"
        String servers = environment.getRequiredProperty("jms.servers.list");

        // Split the property to get individual server names and collect them into a Set
        return new HashSet<>(Arrays.asList(servers.split(";")));
    }


}
