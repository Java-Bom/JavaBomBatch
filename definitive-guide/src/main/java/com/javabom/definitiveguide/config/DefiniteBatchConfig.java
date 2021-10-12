package com.javabom.definitiveguide.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@ComponentScan(value = {
        "com.javabom.definitiveguide.chap2",
        "com.javabom.definitiveguide.chap13"
})
public class DefiniteBatchConfig {
}
