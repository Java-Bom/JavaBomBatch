package com.javabom.definitiveguide.chap7.job.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

public class EmptyInputStepFailer {

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution){
        if(stepExecution.getReadCount() > 0){
            return stepExecution.getExitStatus();
        }
        return ExitStatus.FAILED;
    }
}
