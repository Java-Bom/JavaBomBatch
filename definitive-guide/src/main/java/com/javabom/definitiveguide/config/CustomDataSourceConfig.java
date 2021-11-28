package com.javabom.definitiveguide.config;

import com.javabom.definitiveguide.config.datasource.CustomDataSourceTemplate;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

@Profile("!mysql")
@Configuration
public class CustomDataSourceConfig {
    private static final String DATASOURCE_CONFIG = "spring.datasource.hikari";

    @Nullable
    @Value("${spring.datasource.init-schema:}")
    private Resource customSchemaResource;

    @Bean
    public CustomDataSourceTemplate customDataSourceTemplate(ApplicationContext context) {
        HikariDataSource dataSource = Binder.get(context.getEnvironment())
                .bind(DATASOURCE_CONFIG, Bindable.of(HikariDataSource.class)).get();
        CustomDataSourceTemplate customDataSourceTemplate = new CustomDataSourceTemplate(dataSource);
        if (customSchemaResource != null) {
            customDataSourceTemplate.runScripts(customSchemaResource);
        }
        return customDataSourceTemplate;
    }

}
