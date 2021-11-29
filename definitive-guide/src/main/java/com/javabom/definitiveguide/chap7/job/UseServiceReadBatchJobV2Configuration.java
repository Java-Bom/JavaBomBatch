package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.job.reader.CustomerStubServiceReader;
import com.javabom.service.domain.customer.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UseServiceReadBatchJobV2Configuration {
    public static final String JOB_NAME = "UseServiceReadBatchV2Job";
    private static final String STEP_NAME = JOB_NAME + "Step";
    private static final String SERVICE_READER_NAME = STEP_NAME + "Reader";
    private static final String WRITER_READER_NAME = STEP_NAME + "Writer";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(JOB_NAME)
    public Job useServiceItemReaderJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(useServiceItemReaderStep())
                .build();
    }

    @Bean(STEP_NAME)
    public Step useServiceItemReaderStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<CustomerEntity, CustomerEntity>chunk(100)
                .reader(customerStubServiceReader())
                .writer(customerItemWriter())
                .build();
    }

    @Bean(SERVICE_READER_NAME)
    public CustomerStubServiceReader customerStubServiceReader() {
        CustomerStubServiceReader customerStubServiceReader = new CustomerStubServiceReader();

        customerStubServiceReader.setName(SERVICE_READER_NAME);

        return customerStubServiceReader;
    }

    @Bean(WRITER_READER_NAME)
    public ItemWriter<CustomerEntity> customerItemWriter() {
        return items -> items.forEach(item -> log.info(item.toString()));
    }
}
