package com.javabom.definitiveguide.chap4.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobLoggerListener {

	/**
	 * Expected signature: void beforeJob({@link JobExecution} jobExecution)
	 * @param jobExecution
	 */
	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName() + " is started");
	}

	/**
	 * Expected signature: void afterJob({@link JobExecution} jobExecution)
	 */
	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		System.out.println(jobExecution.getJobInstance().getJobName() + " has completed with the status "
			+ jobExecution.getStatus());
	}
}
