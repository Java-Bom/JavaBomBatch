package com.javabom.definitiveguide.chap5.job;

import com.javabom.definitiveguide.chap6.job.QuartzJobConfiguration;
import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("mysql")
@BatchSpringTest
public class QuartzJobConfigurationTest {

    @Autowired
    private TestJobLauncher testJobLauncher;


    @Test
    public void scheduleTest() {
        JobExecution jobExecution = testJobLauncher.launchJob(QuartzJobConfiguration.JOB_NAME, new JobParameters());

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }
}
