package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class QuoteJobConfiguration {

    public static final String JOB_NAME = "chap7_quote_job";
    public static final String STEP_NAME = "chap7_quote_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(quoteFileStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step quoteFileStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<Customer, Customer>chunk(10)
                .reader(quoteDelimitedCustomerItemReader(null))
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> quoteDelimitedCustomerItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("quoteDelimitedCustomerItemReader")
                .resource(inputFile)
                .delimited()
                .quoteCharacter('#')
                .names("firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city", "state")
                .targetType(Customer.class)
                .build();
    }

    @Bean(name = "quote_item_writer")
    public ItemWriter<Customer> itemWriter() {
        return (items -> items.forEach(System.out::println));
    }
}
