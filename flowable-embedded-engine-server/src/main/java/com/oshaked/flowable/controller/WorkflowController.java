package com.oshaked.flowable.controller;

import com.ecitele.camunda.web.dto.InitiatePaymentDto;
import com.oshaked.flowable.service.OrderProcessInstance;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.StartEvent;
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
        String response = orderProcessInstance.create(processId, initiatePaymentDto);
        System.out.println("Instance was created!");

        return response;
    }

    @PostMapping("/createProcessDef/{processId}")
    public String createProcessDef() {

        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        bpmnModel.addProcess(process);
        process.setId("instantiation");

        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");


        return null;
    }
}
