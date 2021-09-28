package com.javabom.definitiveguide.main;

import com.javabom.definitiveguide.config.DefiniteBatchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@Import(DefiniteBatchConfig.class)
@SpringBootApplication
public class DefiniteGuideBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(DefiniteGuideBatchApplication.class, args);
    }
}

