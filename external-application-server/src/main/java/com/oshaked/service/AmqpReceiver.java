package com.oshaked.service;

import com.oshaked.service.handler.ShipOrderHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AmqpReceiver {

    static final String queueName = "spring-boot";

    @Autowired
    ChargeCardWorker chargeCardWorker;

    public AmqpReceiver() {
    }

    @RabbitListener(queues = queueName)
    public void receive(String message) {
        System.out.println("RabbitListener: shipping");
        chargeCardWorker.creatrWorker(new ShipOrderHandler(), message);
    }
}
