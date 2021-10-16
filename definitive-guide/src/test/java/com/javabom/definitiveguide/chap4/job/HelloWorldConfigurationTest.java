package com.javabom.definitiveguide.chap4.job;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.javabom.definitiveguide.test.BatchSpringTest;
import com.javabom.definitiveguide.test.TestJobLauncher;

@BatchSpringTest
class HelloWorldConfigurationTest {
	@Autowired
	private TestJobLauncher jobLauncher;

	@Test
	void test1() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "bomtest")
			.addDate("timestamp", new Date())
			.toJobParameters();

		//when
		JobExecution execution = jobLauncher.launchJob(HelloWorldConfiguration.JOB_NAME, jobParameters);

		//then
		assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
	}

	@DisplayName("validator 적용 시 required, optional 에도 없는 key 를 넣으면 exception")
	@Test
	void exception() {
		JobParameters jobParameters = new JobParametersBuilder()
			.addString("name", "bomtest")
			.addString("없는파라미터", "nothiing")
			.addDate("timestamp", new Date())
			.toJobParameters();

		assertThatThrownBy(() -> jobLauncher.launchJob(HelloWorldConfiguration.JOB_NAME, jobParameters))
			.isInstanceOf(RuntimeException.class); // launchJob 내에서 RuntimeException 으로 반환됨
	}
}