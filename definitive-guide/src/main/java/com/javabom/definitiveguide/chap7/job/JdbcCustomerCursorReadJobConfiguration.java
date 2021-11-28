package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.mapper.CustomerJdbcRowMapper;
import com.javabom.definitiveguide.chap7.model.CustomerJdbc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcCustomerCursorReadJobConfiguration {
    public static final String JOB_NAME = "JdbcCustomerCursorReadJob";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String CURSOR_READER_NAME = STEP_NAME + "Reader";
    private static final String WRITER_READER_NAME = STEP_NAME + "Writer";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean(JOB_NAME)
    public Job jdbcCustomerCursorReadJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(jdbcCustomerCursorReadStep())
                .build();
    }

    @Bean(STEP_NAME)
    public Step jdbcCustomerCursorReadStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<CustomerJdbc, CustomerJdbc>chunk(100)
                .reader(customerJdbcCursorItemReader(dataSource))
                .writer(customerJdbcCursorItemWriter())
                .build();
    }


    @Bean(CURSOR_READER_NAME)
    public JdbcCursorItemReader<CustomerJdbc> customerJdbcCursorItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<CustomerJdbc>()
                .name(CURSOR_READER_NAME)
                .dataSource(dataSource)
                .sql("SELECT * FROM customer_jdbc WHERE city = ?")
                .preparedStatementSetter(citySetter(null))
                .rowMapper(new CustomerJdbcRowMapper())
                .build();
    }

    @Bean(WRITER_READER_NAME)
    public ItemWriter<CustomerJdbc> customerJdbcCursorItemWriter() {
        return items -> items.forEach(item -> log.info(item.toString()));
    }

    @Bean
    @StepScope
    public ArgumentPreparedStatementSetter citySetter(
            @Value("#{jobParameters['city']}") String city
    ) {
        return new ArgumentPreparedStatementSetter(new Object[]{city});
    }
}
