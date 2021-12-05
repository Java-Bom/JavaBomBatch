package com.javabom.definitiveguide.chap8;

import com.javabom.definitiveguide.config.FileLineMapperGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

@RequiredArgsConstructor
@Configuration
public class ValidatingConfiguration {

    public static final String JOB_NAME = "ValidatingJob";
    public static final String STEP_NAME = "ValidatingStep";

    private final JobBuilderFactory jobFactory;
    private final StepBuilderFactory stepFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        return stepFactory.get(STEP_NAME)
                .<Customer, Customer>chunk(100)
                .reader(csvReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }


    @Bean(STEP_NAME + "Reader")
    @StepScope
    public FlatFileItemReader<Customer> csvReader(
            @Value("#{jobParameters[fileName]}") String fileName
    ) {
        return new FlatFileItemReaderBuilder<Customer>()
                .name(STEP_NAME + "Reader")
                .resource(new FileSystemResource(new File(fileName)))
                .lineMapper(FileLineMapperGenerator.generateLineMapper(Customer.class, ","))
                .build();
    }

    @Bean(STEP_NAME + "Processor")
    @StepScope
    public ItemProcessor<Customer, Customer> processor() {
        ItemProcessor<Customer, Customer> customValidProcessor = new ValidatingItemProcessor<>(new UniqueLastNameValidator());
        ClassifierCompositeItemProcessor<Customer, Customer> processor = new ClassifierCompositeItemProcessor<>();
        processor.setClassifier(item -> {
            if (item.getFirstName().length() > 5) {
                return beanValidProcessor();
            } else {
                return customValidProcessor;
            }
        });
        return processor;
    }

    @Bean(STEP_NAME + "BeanValidProcessor")
    @StepScope
    public ItemProcessor<Customer, Customer> beanValidProcessor() {
        return new BeanValidatingItemProcessor<>();
    }

    @Bean(STEP_NAME + "Writer")
    @StepScope
    public ItemWriter<Customer> writer() {
        return System.out::println;
    }

}
