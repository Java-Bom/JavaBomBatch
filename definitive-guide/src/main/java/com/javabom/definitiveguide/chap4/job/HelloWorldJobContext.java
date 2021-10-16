package com.javabom.definitiveguide.chap4.job;

import java.util.Map;
import java.util.Objects;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloWorldJobContext implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String name = (String)chunkContext.getStepContext()
			.getJobParameters()
			.get("name");

		/**
		 * job 글로벌 Context
		 */
		ExecutionContext jobContext = chunkContext.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext();

		/**
		 * 현재 잡의 Context를 바로 가져올 수는 있지만 조작할 수는 없다
		 * return Collections.unmodifiableMap(result);
		 */
		Map<String, Object> jobExecutionContext = chunkContext.getStepContext()
			.getJobExecutionContext();

		if (Objects.isNull(jobContext.get("user.name"))) {
			log.info("jobContext user.name is empty, start put user.name: " + name);
			jobContext.put("user.name", name); // job 글로벌 Context에 저장
		} else {
			log.info("jobContext user.name is not empty");
		}

		System.out.println("Hello World " + name);

		return RepeatStatus.FINISHED;
	}
}
