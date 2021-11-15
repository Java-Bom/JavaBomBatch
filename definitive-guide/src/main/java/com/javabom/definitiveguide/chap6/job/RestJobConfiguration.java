package com.javabom.definitiveguide.chap6.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestJobConfiguration {
    public static final String JOB_NAME = "chap5_rest_job";
    public static final String JOB_NAME_STEP = "chap5_rest_job_step";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;


    @Bean(name = JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean(name = JOB_NAME_STEP)
    public Step step1() {
        return stepBuilderFactory.get(JOB_NAME_STEP)
                .tasklet((stepContribution, chuckContext) -> {
                    System.out.println(JOB_NAME + " step 1 ran today!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
