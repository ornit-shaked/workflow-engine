package com.oshaked.camunda.misc;

import lombok.experimental.UtilityClass;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

@UtilityClass
public class Flows {

    public static final String WAITING_FOR_SPECIFIC_MESSAGE = "message-name";
    public static final String TASK_IDENTIFIER = "identity";

    public BpmnModelInstance getReceiverFlow(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .receiveTask("receiverTask-id")
                .camundaInputParameter(TASK_IDENTIFIER, "4")
                .message(WAITING_FOR_SPECIFIC_MESSAGE)
                .camundaExecutionListenerDelegateExpression("end","#{receiveOneExecutionListener}")
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }

    public BpmnModelInstance getParallelReceiverFlow(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .parallelGateway("fork")
                .receiveTask("receiverTask-id1")
                .camundaInputParameter(TASK_IDENTIFIER, "4")
                .message(WAITING_FOR_SPECIFIC_MESSAGE)
                .camundaExecutionListenerDelegateExpression("end","#{receiveOneExecutionListener}")
                .parallelGateway("join")
                .moveToNode("fork")
                .receiveTask("receiverTask-id2")
                .camundaInputParameter(TASK_IDENTIFIER, "8")
                .message(WAITING_FOR_SPECIFIC_MESSAGE)
                .camundaExecutionListenerDelegateExpression("end","#{receiveTwoExecutionListener}")
                .connectTo("join")
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }
}
