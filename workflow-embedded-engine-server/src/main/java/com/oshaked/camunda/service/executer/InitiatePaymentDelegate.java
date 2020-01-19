package com.oshaked.camunda.service.executer;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitiatePaymentDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("InitiatePaymentHandler execution "+ execution.getProcessInstanceId());
        log.debug("InitiatePaymentHandler execution "+ execution.getProcessInstanceId());
        Integer price = (Integer) execution.getVariable("price");
        Integer amount = (Integer) execution.getVariable("items");

        boolean isSuccess = (price * amount > 1000) ? false : true;

        // Complete the task
        execution.setVariable("success", isSuccess);
        execution.setVariable("totalPrice", amount * price);
    }
}
