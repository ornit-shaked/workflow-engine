package com.oshaked.camunda.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping({"workflow"})
public class WorkflowController {

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping()
    public void createInstance() {

        runtimeService.startProcessInstanceByKey("test");
    }
}
