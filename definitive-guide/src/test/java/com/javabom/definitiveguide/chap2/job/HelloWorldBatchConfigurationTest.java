package com.javabom.definitiveguide.chap2.job;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@BatchSpringTest
class HelloWorldBatchConfigurationTest {
    @Autowired
    private TestJobLauncher jobLauncher;

    @Test
    void helloWorldJobTest() {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDateTime", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .toJobParameters();

        //when
        JobExecution execution = jobLauncher.launchJob(HelloWorldBatchConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
