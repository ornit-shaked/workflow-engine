package com.oshaked.flowable.service;

import com.ecitele.camunda.web.dto.InitiatePaymentDto;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderProcessInstance {


    @Autowired
    private RuntimeService runtimeService;

    public String create(String processId, InitiatePaymentDto initiatePaymentDto) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("price", initiatePaymentDto.getPrice());
        variables.put("items", initiatePaymentDto.getItems());
        variables.put("arrParallel", initiatePaymentDto.getArrParallel());
        variables.put("arrSequence", initiatePaymentDto.getArrSequence());

        return runtimeService.startProcessInstanceByKey(processId, variables).getProcessInstanceId();
    }
}
