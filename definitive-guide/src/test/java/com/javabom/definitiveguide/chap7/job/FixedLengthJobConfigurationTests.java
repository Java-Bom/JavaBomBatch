package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@BatchSpringTest
class FixedLengthJobConfigurationTests {

    @Autowired
    private TestJobLauncher testJobLauncher;

    @DisplayName("고정 길이 파일 읽기")
    @Test
    public void fixedLengthCustomerItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerFixed.txt")
                .toJobParameters();

        //when
        final JobExecution execution = testJobLauncher.launchJob(FixedLengthJobConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}