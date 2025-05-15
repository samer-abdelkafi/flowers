package com.springit.flowers.config;

import com.springit.flowers.model.ServerBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "flowers")
public class FlowersProperties {

    private List<ServerBuilder> servers;


}