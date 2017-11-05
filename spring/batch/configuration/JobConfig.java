package com.spring.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class JobConfig {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").tasklet((contribution,chunkContext)->{
			System.out.println("HelloWorld");
			return RepeatStatus.FINISHED;
		}).build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").tasklet((contribution,chunkContext)->{
			System.out.println("Hello Chennai");
			return RepeatStatus.FINISHED;
		}).build();
	}
	
	@Bean
	public Step step3() {
		return stepBuilderFactory.get("step3").tasklet((contribution,chunkContext)->{
			System.out.println("Hello Kolkata");
			return RepeatStatus.FINISHED;
		}).build();
	}
	
	@Bean
	public Flow flow() {
		FlowBuilder<Flow> flow=new FlowBuilder<>("flow");
		flow.start(step1()).next(step3()).end();
		
		return flow.build();
	}
	
	@Bean
	public Job job(Flow flow)
	{
		//return jobBuilderFactory.get("job").start(step1()).next(step2()).build();
		//return jobBuilderFactory.get("job").start(step1()).on("COMPLETED").to(step2()).from(step2()).on("COMPLETED").fail().from(step3()).end().build();
		//return jobBuilderFactory.get("job").start(step1()).on("COMPLETED").to(step2()).from(step2()).on("COMPLETED").stopAndRestart(step3()).from(step3()).end().build();
		return jobBuilderFactory.get("job").start(step1()).on("COMPLETED").to(flow()).from(flow()).end().build();
		//return jobBuilderFactory.get("job").start(step1()).split(new SimpleAsyncTaskExecutor()).add(flow()).end().build();
		//return jobBuilderFactory.get("job").start(step1()).split(new SimpleAsyncTaskExecutor()).add(step2()).
	}

}
