package com.oshaked.camunda.misc;

import lombok.experimental.UtilityClass;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;

@UtilityClass
public class ModelFlows {

//    public BpmnModelInstance modelInstance;
//    public Definitions definitions;
//    public Process process;

    protected <T extends BpmnModelElementInstance> T createElement(BpmnModelInstance modelInstance,
                                                                   BpmnModelElementInstance parentElement,
                                                                   String id,
                                                                   Class<T> elementClass) {
        T element = modelInstance.newInstance(elementClass);
        element.setAttributeValue("id", id, true);
        parentElement.addChildElement(element);
        return element;
    }

//    public SequenceFlow createSequenceFlow(Process process, FlowNode from, FlowNode to) {
//        SequenceFlow sequenceFlow = createElement(process, from.getId() + "-" + to.getId(), SequenceFlow.class);
//        process.addChildElement(sequenceFlow);
//        sequenceFlow.setSource(from);
//        from.getOutgoing().add(sequenceFlow);
//        sequenceFlow.setTarget(to);
//        to.getIncoming().add(sequenceFlow);
//        return sequenceFlow;
//    }
}
