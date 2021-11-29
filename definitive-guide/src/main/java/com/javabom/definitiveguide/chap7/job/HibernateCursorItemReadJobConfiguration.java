package com.javabom.definitiveguide.chap7.job;

import com.javabom.service.domain.customer.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HibernateCursorItemReadJobConfiguration {
    public static final String JOB_NAME = "HibernateCustomerCursorReadJob";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String CURSOR_READER_NAME = STEP_NAME + "Reader";
    private static final String WRITER_READER_NAME = STEP_NAME + "Writer";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    public Job hibernateCustomerCursorReadJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(hibernateCustomerCursorReadStep())
                .build();
    }

    @Bean(STEP_NAME)
    public Step hibernateCustomerCursorReadStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(customerHibernateCursorItemReader(null, null))
                .writer(customerItemWriter())
                .build();
    }

    @StepScope
    @Bean(CURSOR_READER_NAME)
    public HibernateCursorItemReader<CustomerEntity> customerHibernateCursorItemReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters['city']}") String city
    ) {
        return new HibernateCursorItemReaderBuilder<CustomerEntity>()
                .name(CURSOR_READER_NAME)
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .queryString("FROM customer_entity WHERE city = :city")
                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }

    @Bean(WRITER_READER_NAME)
    public ItemWriter<CustomerEntity> customerItemWriter() {
        return items -> items.forEach(item -> log.info(item.toString()));
    }
}
