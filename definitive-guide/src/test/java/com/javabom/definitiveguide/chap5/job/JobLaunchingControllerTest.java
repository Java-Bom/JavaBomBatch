package com.javabom.definitiveguide.chap5.job;

import com.javabom.definitiveguide.chap6.controller.JobLaunchRequest;
import com.javabom.definitiveguide.chap6.controller.JobLaunchingController;
import com.javabom.definitiveguide.chap6.job.RestJobConfiguration;
import com.javabom.definitiveguide.test.BatchSpringTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@BatchSpringTest
public class JobLaunchingControllerTest {

    @Autowired
    private JobLaunchingController jobLaunchingController;

    @Test
    @DisplayName("REST 방식으로 잡 실행하기 테스트")
    public void runJobTest() throws Exception {
        JobLaunchRequest request = new JobLaunchRequest();
        request.setName(RestJobConfiguration.JOB_NAME);
        request.setJobParameters(new Properties());

        ExitStatus exitStatus = jobLaunchingController.runJob(request);

        assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
    }

    @Test
    @DisplayName("JobParametersIncrementer 적용한 잡 실행 테스트")
    public void runJobParametersIncrementerTest() throws Exception {
        JobLaunchRequest request = new JobLaunchRequest();
        request.setName(RestJobConfiguration.JOB_NAME);
        request.setJobParameters(new Properties());

        ExitStatus exitStatus = jobLaunchingController.runJobIncrement(request);

        assertThat(exitStatus).isEqualTo(ExitStatus.COMPLETED);
    }
}

