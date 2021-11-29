package com.javabom.service.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(value = {
        "com.javabom.service.domain"
})
@EnableJpaRepositories(value = {
        "com.javabom.service.domain"
})
@EnableJpaAuditing
@Configuration
@ComponentScan(value = {
        "com.javabom.service.domain"
})
public class BatchServiceConfig {
}

