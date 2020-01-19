package com.oshaked.camunda.controller;

import com.ecitele.camunda.web.dto.InitiatePaymentDto;
import com.oshaked.camunda.service.OrderProcessInstance;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.ProcessImpl;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"workflow"})
public class WorkflowController {

    @Autowired
    OrderProcessInstance orderProcessInstance;

    @PostMapping("/createInstance/{processId}")
    public String createInstance(@PathVariable("processId") String processId,
                                             @RequestBody InitiatePaymentDto initiatePaymentDto) {
        System.out.println("Create Instance ...");
        String response = orderProcessInstance.create(processId, initiatePaymentDto);

        return response;
    }
}
