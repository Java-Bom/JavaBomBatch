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
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class FixedLengthJobConfiguration {

    public static final String JOB_NAME = "chap7_fixed_length_job";
    public static final String STEP_NAME = "chap7_fixed_length_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(copyFileStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step copyFileStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<Customer, Customer>chunk(10)
                .reader(fixedLengthCustomerItemReader(null))
                .writer(itemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> fixedLengthCustomerItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("fixedLengthCustomerItemReader")
                .resource(inputFile)
                .fixedLength()
                .columns(new Range[]{new Range(1, 11), new Range(12, 12), new Range(13, 22),
                        new Range(23, 26), new Range(27, 46), new Range(47, 62), new Range(63, 64),
                        new Range(65, 69)})
                .names("firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city", "state",
                        "zipCode")
                .targetType(Customer.class)
                .build();
    }

    @Bean(name = "fixed_item_writer")
    public ItemWriter<Customer> itemWriter() {
        return (items -> items.forEach(System.out::println));
    }
}
