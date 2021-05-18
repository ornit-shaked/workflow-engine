package com.oshaked.camunda.service;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FlowService {

    @Autowired
    private ProcessInstanceService processService;

    public ProcessInstance run(String key) {
        return processService.startProcessInstanceByKey(key, null);
    }
}
