package com.javabom.definitiveguide.chap4.job;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class TimeStamper implements JobParametersIncrementer {
	@Override
	public JobParameters getNext(JobParameters parameters) {
		return new JobParametersBuilder(parameters)
			.addDate("timestamp", new Date())
			.toJobParameters();
	}
}
