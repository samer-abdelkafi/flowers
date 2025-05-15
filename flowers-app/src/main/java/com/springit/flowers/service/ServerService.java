package com.springit.flowers.service;

import com.springit.flowers.config.FlowersProperties;
import com.springit.flowers.exception.NotFoundException;
import com.springit.flowers.model.Server;
import com.springit.flowers.model.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final FlowersProperties flowersProperties;

    private Set<Server> servers;

    public Server getServer(String name) {
        return getServers().stream()
                .filter(s -> name.equals(s.getName()))
                .findAny()
                .orElseThrow(() -> new NotFoundException(name + " server not configured!!!"));
    }

    public Set<Server> getServers() {
        if(servers == null) {
            servers = flowersProperties.getServers().stream()
                    .map(ServerBuilder::build)
                    .collect(Collectors.toSet());
        }
        return servers;
    }

}