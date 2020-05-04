package com.oshaked.camunda.tempFactory;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;

public interface Activity {

    <T extends BpmnModelElementInstance> T buildInternalFlow(BpmnModelInstance modelInstance,
                                                             BpmnModelElementInstance parentElement,
                                                             String nodeInstanceId);

    String getObjectType();
}
