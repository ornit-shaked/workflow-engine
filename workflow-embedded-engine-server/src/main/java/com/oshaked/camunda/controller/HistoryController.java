package com.oshaked.camunda.controller;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricExternalTaskLog;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping({"history"})
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine camunda;


    @GetMapping("process-instance/{id}")
    public List<HistoricProcessInstance> getProcessInstance(@PathVariable("id") String id) {

        return camunda.getHistoryService().createHistoricProcessInstanceQuery().processDefinitionKey(id).list();
    }

    @GetMapping("external-task/{id}")
    public List<HistoricExternalTaskLog> getHistoricExternalTaskLog(@PathVariable("id") String id) {

        return camunda.getHistoryService().createHistoricExternalTaskLogQuery().list();
    }


    /**
     * Get all activityId that waiting to be execute
     * @param id
     * @return
     */
    @GetMapping("activity-task/{id}")
    public List<String> getExecusions(@PathVariable("id") String id) {
        return camunda.getRuntimeService().getActiveActivityIds(id);
    }
}
