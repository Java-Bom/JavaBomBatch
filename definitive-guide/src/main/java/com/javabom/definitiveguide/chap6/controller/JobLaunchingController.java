package com.javabom.definitiveguide.chap6.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobLaunchingController {

    private final JobLauncher jobLauncher;

    private final ApplicationContext context;

    private final JobExplorer jobExplorer;

    @PostMapping("/run")
    public ExitStatus runJob(@RequestBody JobLaunchRequest request) throws Exception {
        Job job = context.getBean(request.getName(), Job.class);
        return jobLauncher.run(job, request.getJobParameters()).getExitStatus();
    }

    @PostMapping("/run/increment")
    public ExitStatus runJobIncrement(@RequestBody JobLaunchRequest request) throws Exception {
        Job job = context.getBean(request.getName(), Job.class);
        JobParameters jobParameters = new JobParametersBuilder(request.getJobParameters(), jobExplorer)
                .getNextJobParameters(job)
                .toJobParameters();
        return jobLauncher.run(job, jobParameters).getExitStatus();
    }

}
