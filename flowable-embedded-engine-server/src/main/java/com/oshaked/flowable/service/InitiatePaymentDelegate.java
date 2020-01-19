package com.oshaked.flowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class InitiatePaymentDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution){

        System.out.println("InitiatePaymentHandler execution "+ execution.getProcessInstanceId());
        Integer price = (Integer) execution.getVariable("price");
        Integer amount = (Integer) execution.getVariable("items");

        boolean isSuccess = (price * amount > 1000) ? false : true;

        // Complete the task
        execution.setVariable("success", isSuccess);
        execution.setVariable("totalPrice", amount * price);
    }
}
