package com.oshaked.service.handler;

import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

import java.util.HashMap;
import java.util.Map;

public class ShipOrderHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        System.out.println("ShipOrderHandler handler");
        Integer totalPrice = externalTask.getVariable("totalPrice");

        // Complete the task
        Map<String, Object> vars = new HashMap<>();
        vars.put("shipment", "Order was shipped, total price is:" + totalPrice );

        externalTaskService.complete(externalTask, vars);
    }
}
