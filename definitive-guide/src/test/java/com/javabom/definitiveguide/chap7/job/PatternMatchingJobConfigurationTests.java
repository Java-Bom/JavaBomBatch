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
class PatternMatchingJobConfigurationTests {

    @Autowired
    private TestJobLauncher jobLauncher;

    @DisplayName("PatternMatchingCompositeLineMapper을 통한 여러 레코드 포맷 파일 읽기")
    @Test
    public void patternMatchingItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerMultiFormat1.csv")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(PatternMatchingJobConfiguration.JOB_NAME, jobParameters);

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}