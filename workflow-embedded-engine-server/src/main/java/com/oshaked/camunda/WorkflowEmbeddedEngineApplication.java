package com.oshaked.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableProcessApplication
public class WorkflowEmbeddedEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowEmbeddedEngineApplication.class, args);
	}

	@EventListener
	public void onPostDeploy(PostDeployEvent event) {
		System.out.println("PostDeployEvent");
	}

	@EventListener
	public void onPreUndeploy(PreUndeployEvent event) {
		System.out.println("PreUndeployEvent");
	}

}
