package com.oshaked.camunda.service.we.listener;

import com.oshaked.camunda.service.consumers.ReceiverTaskConsumer;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Component
public class ReceiveTwoExecutionListener extends BaseExecutionListener {

    @Override
    protected void logic(DelegateExecution execution) {

        System.out.println("ReceiveTwoExecutionListener");

        //Access to Receiver Task local variable
        String messageInput = (String) execution.getVariableLocal(ReceiverTaskConsumer.MESSAGE_INPUT_NAME);
        System.out.println("messageInput: "+messageInput);
    }
}
