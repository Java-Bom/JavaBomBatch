package com.javabom.definitiveguide.chap4.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ExecutionConfiguration {
	protected static final String JOB_NAME = "chap4_job_execution";
	protected static final String JOB_NAME_STEP = "chap4_job_execution_step";
	/**
	 * 실제로 스프링 배치의 잡과 스텝을 생성하는데 사용되는 JobBuilder, StepBuilder 인스턴스 생성
	 */
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean(name = JOB_NAME)
	public Job job() {
		return this.jobBuilderFactory.get(JOB_NAME)
			.incrementer(new RunIdIncrementer())
			.start(step1())
			.next(step2())
			.build();
	}

	@Bean(name = JOB_NAME + "-step1")
	public Step step1() {
		return stepBuilderFactory.get(JOB_NAME + "-step1")
			.tasklet(new HelloWorldJobContext())
			.build();
	}

	@Bean(name = JOB_NAME + "-step2")
	public Step step2() {
		return stepBuilderFactory.get(JOB_NAME + "-step2")
			.tasklet(new HelloWorldJobContext())
			.build();
	}

	@Bean(name = JOB_NAME_STEP)
	public Job jobStep() {
		return this.jobBuilderFactory.get(JOB_NAME_STEP)
			.incrementer(new RunIdIncrementer())
			.start(step1Step())
			.next(step2Step())
			.build();
	}

	@Bean(name = JOB_NAME_STEP + "-step1")
	public Step step1Step() {
		return stepBuilderFactory.get(JOB_NAME_STEP + "-step1")
			.tasklet(new HelloWorldStepContext())
			.listener(promotionListner())
			.build();
	}

	@Bean(name = JOB_NAME_STEP + "-step2")
	public Step step2Step() {
		return stepBuilderFactory.get(JOB_NAME_STEP + "-step2")
			.tasklet(new HelloWorldStepContext())
			.build();
	}

	/**
	 * step 이 성공적으로 마치면 job context에 step context 내용을 저장함
	 * @return
	 */
	public StepExecutionListener promotionListner() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

		listener.setKeys(new String[] {"step.name"});

		return listener;
	}
}
