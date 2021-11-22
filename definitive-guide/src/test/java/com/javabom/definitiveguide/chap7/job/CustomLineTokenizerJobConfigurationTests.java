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
class CustomLineTokenizerJobConfigurationTests {

    @Autowired
    private TestJobLauncher jobLauncher;

    @DisplayName("커스텀 레코드 파싱 테스트")
    @Test
    public void customLineTokenizerItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerDelimited.txt")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(CustomLineTokenizerJobConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}