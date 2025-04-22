package com.springit.flowers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Slf4j
@Configuration
@EnableJpaRepositories(basePackages = {"com.springit.flowers.repo"})
public class AuditDataSourceConfig {


}
