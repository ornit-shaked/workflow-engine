package com.oshaked.camunda.controller;

import com.oshaked.camunda.service.FlowService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"flow"})
public class FlowController {

    @Autowired
    private FlowService flowService;

    @GetMapping("run/{key}")
    public String run(@PathVariable("key") String key) {
        ProcessInstance run = flowService.run(key);
        return run.getProcessDefinitionId();
    }
}
