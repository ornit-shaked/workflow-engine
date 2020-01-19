package com.oshaked.camunda.service.executer;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WarehouseOrderDelegate implements JavaDelegate {

    static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";

    static final String routingKey = "createShipment";

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("WarehouseOrderDelegate execution "  +execution.getProcessInstanceId());
        Integer items = (Integer) execution.getVariable("items");

        rabbitTemplate.convertAndSend(topicExchangeName, "createShipment", "ship-product");

    }
}
