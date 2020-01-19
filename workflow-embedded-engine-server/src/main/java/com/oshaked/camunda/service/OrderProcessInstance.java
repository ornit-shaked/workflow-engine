package com.oshaked.camunda.service;

import com.ecitele.camunda.web.dto.InitiatePaymentDto;
import com.ecitele.camunda.web.misc.ProcessConstants;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessInstance {

    @Autowired
    private ProcessEngine camunda;

    public String create(String processId, InitiatePaymentDto initiatePaymentDto) {
        String processInstanceId = camunda.getRuntimeService().startProcessInstanceByKey(
                processId,
                Variables
                        .putValue("price", initiatePaymentDto.getPrice())
                        .putValue("items", initiatePaymentDto.getItems())).getProcessInstanceId();

        return processInstanceId;
    }
}
