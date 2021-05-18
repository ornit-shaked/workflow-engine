package com.oshaked.camunda.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProcessInstanceService {

    @Autowired
    RuntimeService runtimeService;

    /**
     * @param key   key of the process
     * @param input input composed of key String and value of Map<String, Object>
     * @return processInstance
     */
    public ProcessInstance startProcessInstanceByKey(String key, Map<String, Object> input) {

        log.debug("Trying to start process with key'{}'", key);

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key, input);

        if (processInstance == null) {
            throw new RuntimeException("Cannot start process with key: " + key);
        }

        log.debug("Instance key '{}' started successfully", key);

        return processInstance;
    }

    public void getAllProcessInstance() {
        ProcessInstanceQuery a = runtimeService.createProcessInstanceQuery().active();
    }
}
