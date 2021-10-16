package com.javabom.definitiveguide.chap4.job;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;

@BatchSpringTest
class ExecutionConfigurationTest {
	@Autowired
	TestJobLauncher jobLauncher;

	@DisplayName("job context")
	@Test
	void job() {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "bomtest")
			.addDate("timestamp", new Date())
			.toJobParameters();

		JobExecution jobExecution = jobLauncher.launchJob(ExecutionConfiguration.JOB_NAME, jobParameters);

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

	@DisplayName("Step context")
	@Test
	void step() {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "bomtest")
			.addDate("timestamp", new Date())
			.toJobParameters();

		JobExecution jobExecution = jobLauncher.launchJob(ExecutionConfiguration.JOB_NAME_STEP, jobParameters);

		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
	}

}