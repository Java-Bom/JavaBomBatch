package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.mapper.CustomerFileLineTokenizer;
import com.javabom.definitiveguide.chap7.model.CustomerAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class CustomLineTokenizerJobConfiguration {

    public static final String JOB_NAME = "chap7_custom_lineTokenizer_job";
    public static final String STEP_NAME = "chap7_custom_lineTokenizer_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(customLineTokenizerFileStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step customLineTokenizerFileStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<CustomerAddress, CustomerAddress>chunk(10)
                .reader(customLineTokenizerItemReader(null))
                .writer(customLineTokenizerItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerAddress> customLineTokenizerItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        return new FlatFileItemReaderBuilder<CustomerAddress>()
                .name("customLineTokenizerItemReader")
                .lineTokenizer(new CustomerFileLineTokenizer(new DefaultFieldSetFactory()))
                .targetType(CustomerAddress.class)
                .resource(inputFile)
                .build();
    }

    @Bean(name = "customer_lineTokenizer_item_writer")
    public ItemWriter<CustomerAddress> customLineTokenizerItemWriter() {
        return (items -> items.forEach(System.out::println));
    }
}
