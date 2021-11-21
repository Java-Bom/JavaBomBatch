package com.javabom.definitiveguide.chap7.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabom.definitiveguide.chap7.model.CustomerTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;

@Configuration
@RequiredArgsConstructor
public class JsonJobConfiguration {

    public static final String JOB_NAME = "chap7_json_job";
    public static final String STEP_NAME = "chap7_json_step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(jsonFileStep())
                .build();
    }

    @Bean(name = STEP_NAME)
    public Step jsonFileStep() {
        return this.stepBuilderFactory.get(STEP_NAME)
                .<CustomerTransactions, CustomerTransactions>chunk(10)
                .reader(jsonFileReader(null))
                .writer(jsonItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<CustomerTransactions> jsonFileReader(
            @Value("#{jobParameters['customerFile']}") ClassPathResource inputFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

        JacksonJsonObjectReader<CustomerTransactions> jsonObjectReader = new JacksonJsonObjectReader<>(CustomerTransactions.class);
        jsonObjectReader.setMapper(objectMapper);

        return new JsonItemReaderBuilder<CustomerTransactions>()
                .name("customerFileReader")
                .jsonObjectReader(jsonObjectReader)
                .resource(inputFile)
                .build();
    }

    @Bean
    public ItemWriter jsonItemWriter() {
        return (items) -> items.forEach(System.out::println);
    }

}
