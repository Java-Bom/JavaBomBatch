package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.job.query.CustomerByCityQueryProvider;
import com.javabom.service.domain.customer.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {
    public static final String JOB_NAME = "JpaCustomerPagingReadJob";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String Paging_READER_NAME = STEP_NAME + "Reader";
    private static final String WRITER_READER_NAME = STEP_NAME + "Writer";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean(STEP_NAME)
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(jpaPagingItemReader(null, null))
                .writer(customerItemWriter())
                .build();
    }

    @StepScope
    @Bean(Paging_READER_NAME)
    public JpaPagingItemReader<CustomerEntity> jpaPagingItemReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters['city']}") String city
    ) {
        CustomerByCityQueryProvider cityQueryProvider = new CustomerByCityQueryProvider(city);
        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name(Paging_READER_NAME)
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(cityQueryProvider)
                .build();
    }

    @Bean(WRITER_READER_NAME)
    public ItemWriter<CustomerEntity> customerItemWriter() {
        return items -> items.forEach(item -> log.info(item.toString()));
    }
}
