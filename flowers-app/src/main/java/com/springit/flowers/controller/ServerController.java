package com.springit.flowers.controller;


import com.springit.flowers.model.Server;
import com.springit.flowers.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = "/servers")
@AllArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    public Set<Server> getServers() {
        return serverService.getServers();
    }

}
