package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.chap7.mapper.CustomerFieldSetMapper;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class MapperJobConfiguration {

    public static final String JOB_NAME = "chap7_mapper_job";
    public static final String STEP_NAME = "chap7_mapper_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(mapperFileStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step mapperFileStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<CustomerAddress, CustomerAddress>chunk(10)
                .reader(mapperCustomerItemReader(null))
                .writer(mapperItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerAddress> mapperCustomerItemReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        return new FlatFileItemReaderBuilder<CustomerAddress>()
                .name("mapperCustomerItemReader")
                .resource(inputFile)
                .delimited()
                .names("firstName", "middleInitial", "lastName",
                        "addressNumber", "street", "city", "state",
                        "zipCode")
                .fieldSetMapper(new CustomerFieldSetMapper())
                .build();
    }

    @Bean(name = "mapper_item_writer")
    public ItemWriter<CustomerAddress> mapperItemWriter() {
        return (items -> items.forEach(System.out::println));
    }
}
