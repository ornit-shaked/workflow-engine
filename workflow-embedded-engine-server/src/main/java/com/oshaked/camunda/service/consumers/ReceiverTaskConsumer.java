package com.oshaked.camunda.service.consumers;

import com.oshaked.camunda.conf.KafkaConsumerConfig;
import com.oshaked.camunda.conf.OnStartupApp;
import com.oshaked.camunda.elements.ReceiverMessage;
import com.oshaked.camunda.misc.Flows;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceiverTaskConsumer {

    public static final String MESSAGE_INPUT_NAME = "messageInputName";
    public static final String MESSAGE_INPUT_VALUE = "messageInputValue";

    @Autowired
    RuntimeService runtimeService;

    @KafkaListener(containerFactory = KafkaConsumerConfig.RECEIVER_MESSAGE,
            topics = "${kafka.topic.wf.receiver}",
            autoStartup = "${kafka.enabled}")
    private void consume(ReceiverMessage receiverMessage) {
        System.out.println("receiverMessage listener");
        try {

           MessageCorrelationResult messageCorrelationResult = runtimeService
                    .createMessageCorrelation(Flows.WAITING_FOR_SPECIFIC_MESSAGE)
                    .processInstanceId(receiverMessage.getCorrelationId())//differ between ihe instances
                    .localVariableEquals(Flows.TASK_IDENTIFIER,receiverMessage.getIdentity())
                    .setVariableLocal(MESSAGE_INPUT_NAME, MESSAGE_INPUT_VALUE)
                    .correlateWithResult();

        } catch (MismatchingMessageCorrelationException e) {
            System.out.println("Cannot correlate message "+Flows.WAITING_FOR_SPECIFIC_MESSAGE
                    + " for processInstanceId="+receiverMessage.getCorrelationId()
                    + " and "+Flows.TASK_IDENTIFIER+"="+receiverMessage.getIdentity());
        }
    }
}
