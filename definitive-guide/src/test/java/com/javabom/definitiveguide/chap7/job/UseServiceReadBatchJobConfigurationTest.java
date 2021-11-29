package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@BatchSpringTest
class UseServiceReadBatchJobConfigurationTest {
    private final TestJobLauncher jobLauncher;

    UseServiceReadBatchJobConfigurationTest(TestJobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    @Test
    void 기존서비스를_리더로_사용하는_배치_테스트() {
        //when
        JobExecution execution = jobLauncher.launchJob(UseServiceReadBatchJobConfiguration.JOB_NAME,
                new JobParametersBuilder()
                        .toJobParameters()
        );

        //then
        assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

}
