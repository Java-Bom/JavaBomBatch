package com.javabom.definitiveguide.chap13.test;

import com.javabom.definitiveguide.config.FileLineMapperGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

@RequiredArgsConstructor
@Configuration
public class FileReadBatchConfiguration {
    public static final String JOB_NAME = "fileReadJob";
    public static final String STEP_NAME = "fileReadJobStep";

    private final JobBuilderFactory jobFactory;
    private final StepBuilderFactory stepFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return this.jobFactory.get(JOB_NAME)
                .start(step())
                .next(step2())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        return stepFactory.get(STEP_NAME)
                .<Member, Member>chunk(100)
                .reader(csvReader(null))
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "Second")
    @JobScope
    public Step step2() {
        return stepFactory.get(STEP_NAME + "Second")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Hello World!");
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Bean(STEP_NAME + "Reader")
    @StepScope
    public FlatFileItemReader<Member> csvReader(@Value("#{jobParameters[fileName]}") String fileName) {
        return new FlatFileItemReaderBuilder<Member>()
                .name("csvReader")
                .resource(new FileSystemResource(new File(fileName)))
                .lineMapper(FileLineMapperGenerator.generateLineMapper(Member.class, ","))
                .build();
    }

    @Bean(STEP_NAME + "Writer")
    @StepScope
    public ItemWriter<Member> writer() {
        return System.out::println;
    }

}
