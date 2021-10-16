package com.javabom.definitiveguide.chap4.job;

import java.util.Objects;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloWorldStepContext implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String name = (String)chunkContext.getStepContext()
			.getJobParameters()
			.get("name");

		/**
		 * Step 의 개별 Context
		 */
		ExecutionContext context = chunkContext.getStepContext()
			.getStepExecution()
			.getExecutionContext();

		System.out.println("HelloWorld! " + name);

		if (Objects.isNull(chunkContext.getStepContext().getJobExecutionContext().get("step.name"))) {
			log.info("step.name is empty, data put start");
			context.put("step.name", name); // 개별 Step Context 이기 때문에 JobContext로 승격시켜야함
		} else {
			log.info("step.name is not empty");
		}

		return RepeatStatus.FINISHED;
	}
}
