package com.javabom.definitiveguide.chap5.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExplorerBatchConfiguration {
    private static final String JOB_NAME = "chap5_job";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExplorer jobExplorer;

    @Bean
    public Tasklet explorerTasklet(){
        return new ExploringTasklet(this.jobExplorer);
    }

    @Bean
    public Step explorerStep(){
        return this.stepBuilderFactory.get("explorerStep")
                .tasklet(explorerTasklet())
                .build();
    }

    @Bean(name = JOB_NAME)
    public Job explorerJob(){
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(explorerStep())
                .build();
    }

}
