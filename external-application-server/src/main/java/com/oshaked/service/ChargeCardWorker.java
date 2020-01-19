package com.oshaked.service;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.stereotype.Service;

@Service
public class ChargeCardWorker {

    public void creatrWorker(ExternalTaskHandler handler, String type) {

        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8084/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();

        //subscribe to an external task topic as specified in the process
        client.subscribe(type)
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler(handler)
                .open();
    }
}