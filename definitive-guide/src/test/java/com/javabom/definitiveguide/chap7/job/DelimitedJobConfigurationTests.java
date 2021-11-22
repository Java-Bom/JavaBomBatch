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
class DelimitedJobConfigurationTests {

    @Autowired
    private TestJobLauncher jobLauncher;

    @DisplayName("쉼표로 구분된 파일 읽기")
    @Test
    public void delimitedCustomerItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerDelimited.txt")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(DelimitedJobConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @DisplayName("인용구 지정된 파일 읽기")
    @Test
    public void quoteDelimitedCustomerItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerDelimitedWithQuote.txt")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(DelimitedJobConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    @DisplayName("매퍼를 사용한 파일 읽기")
    @Test
    public void mapperCustomerItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerDelimited.txt")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(DelimitedJobConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}