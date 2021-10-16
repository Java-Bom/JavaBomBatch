package com.javabom.definitiveguide.chap4.job;

import java.util.Arrays;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HelloWorldConfiguration {
	static final String JOB_NAME = "chap4_job";
	/**
	 * 실제로 스프링 배치의 잡과 스텝을 생성하는데 사용되는 JobBuilder, StepBuilder 인스턴스 생성
	 */
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean(name = JOB_NAME)
	public Job job() {
		return this.jobBuilderFactory.get(JOB_NAME) // Job 의 이름 지정
			/**
			 * 스프링 배치가 잡 시작 시에 유효성 검증 수행
			 */
			//.validator(nameFormatValidator)
			//.validator(new DefaultJobParametersValidator(new String[]{"name"}, new String[]{"date"})) // 필수값 유무를 판단할 수 있는 기본 유효성 검증기
			.start(step1())
			.next(step2(null))
			.validator(compositeJobParametersValidator())
			.incrementer(new RunIdIncrementer()) // 스프링 배치에서 기본적으로 제공하는 run.id 를 1씩 증가시키는 incrementer
			.listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
			.build(); // 최종적으로 SimpleJob 생성
	}

	@Bean(name = JOB_NAME + "-step1")
	public Step step1() {
		return stepBuilderFactory.get(JOB_NAME + "-step1")
			.tasklet(helloWorldTasklet())
			.build();
	}

	/**
	 * JobScope 로 설정해서 JobParameters를 latebinding
	 * @param name
	 * @return
	 */
	@Bean(name = JOB_NAME + "-step2")
	@JobScope
	public Step step2(@Value("#{jobParameters['name']}") String name) {
		return stepBuilderFactory.get(JOB_NAME + "-step2")
			.tasklet(((contribution, chunkContext) -> {
				System.out.println("Step2 : " + name);
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	private Tasklet helloWorldTasklet() {
		/**
		 * contribution: StepContribution
		 * 아직 커밋되지 않은 현재 트랜잭션에 대한 정보(읽기수, 읽기수)
		 *
		 * chunkContext: ChunkContext
		 * 실행 시점의 잡 상태
		 * 태스크릿 내의 처리중인 청크와 관련된 정보, 청크 정보 중에는 스텝, 잡 정보도 있음
		 */
		return (contribution, chunkContext) -> { // tasklet 을 사용하는 스텝
			Map<String, Object> jobParameters = chunkContext.getStepContext() // chunkContext에서 파라미터 값 얻기
				.getJobParameters();
			String name = (String)jobParameters.get("name");

			System.out.println(name + " Hello, World! ");
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public CompositeJobParametersValidator compositeJobParametersValidator() {
		CompositeJobParametersValidator compositeJobParametersValidator = new CompositeJobParametersValidator();

		compositeJobParametersValidator.setValidators(Arrays.asList(defaultJobParametersValidator(), new NameFormatValidator()));

		return compositeJobParametersValidator;
	}

	@Bean
	public DefaultJobParametersValidator defaultJobParametersValidator() {
		return new DefaultJobParametersValidator(new String[] {"name"}, new String[] {"date", "run.id", "timestamp"});
	}
}
