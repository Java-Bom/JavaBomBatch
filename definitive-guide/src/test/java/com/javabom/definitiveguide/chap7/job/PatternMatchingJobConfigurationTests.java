package com.javabom.definitiveguide.chap7.job;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        final List<StepExecution> stepExecutions = new ArrayList<>(execution.getStepExecutions());

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecutions.get(0).getReadCount()).isEqualTo(9);
        assertThat(stepExecutions.get(0).getWriteCount()).isEqualTo(9);
    }

    @DisplayName("ItemStreamReader 인터페이스 구현을 통한 여러 레코드 포맷 파일 읽기")
    @Test
    public void customerFileItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile", "input/customerMultiFormat1.csv")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(PatternMatchingJobConfiguration.JOB_NAME, jobParameters);
        final List<StepExecution> stepExecutions = new ArrayList<>(execution.getStepExecutions());

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecutions.get(0).getReadCount()).isEqualTo(3);
        assertThat(stepExecutions.get(0).getWriteCount()).isEqualTo(3);
    }

    @DisplayName("여러 파일 읽기")
    @Test
    public void multiFileItemReaderTest() throws Exception {
        //given
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString("customerFile",
                        "input/customerMultiFormat1.csv," +
                                "input/customerMultiFormat2.csv," +
                                "input/customerMultiFormat3.csv")
                .toJobParameters();

        //when
        final JobExecution execution = jobLauncher.launchJob(PatternMatchingJobConfiguration.JOB_NAME, jobParameters);
        final List<StepExecution> stepExecutions = new ArrayList<>(execution.getStepExecutions());

        //then
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }
}