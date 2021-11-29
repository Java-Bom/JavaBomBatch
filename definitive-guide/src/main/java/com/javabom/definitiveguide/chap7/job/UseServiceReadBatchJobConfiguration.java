package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.service.CustomerStubService;
import com.javabom.service.domain.customer.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class UseServiceReadBatchJobConfiguration {
    public static final String JOB_NAME = "UseServiceReadBatchJob";
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
                .reader(serviceCursorItemReader(null))
                .writer(customerItemWriter())
                .build();
    }

    @Bean("stubService")
    public CustomerStubService stubService() {
        return new CustomerStubService();
    }

    @Bean(SERVICE_READER_NAME)
    public ItemReaderAdapter<CustomerEntity> serviceCursorItemReader(
            @Qualifier("stubService") CustomerStubService customerStubService
    ) {
        ItemReaderAdapter<CustomerEntity> adapter = new ItemReaderAdapter<>();

        adapter.setTargetObject(customerStubService);
        adapter.setTargetMethod("getCustomer");

        return adapter;
    }

    @Bean(WRITER_READER_NAME)
    public ItemWriter<CustomerEntity> customerItemWriter() {
        return items -> items.forEach(item -> log.info(item.toString()));
    }
}
