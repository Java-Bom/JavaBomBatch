package com.javabom.definitiveguide.config.datasource;

import lombok.Getter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

@Getter
public class DefaultDataSourceTemplate {
    private final DataSource dataSource;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate;

    public DefaultDataSourceTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.transactionManager = new DataSourceTransactionManager(dataSource);
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public void runScripts(String script) {
        runScripts(new ByteArrayResource(script.getBytes()));
    }

    public void runScripts(Resource... scripts) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(scripts);
        populator.setCommentPrefix("#");
        transactionTemplate.executeWithoutResult(status -> DatabasePopulatorUtils.execute(populator, dataSource));
    }

    @PreDestroy
    public void destroy() {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                throw new IllegalStateException("datasource close error.", e);
            }
        }
    }
}
