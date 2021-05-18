package com.oshaked.camunda.misc;

import lombok.experimental.UtilityClass;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SubProcess;

@UtilityClass
public class FluentFlows {

    public static final String WAITING_FOR_SPECIFIC_MESSAGE = "message-name";
    public static final String TASK_IDENTIFIER = "identity";

    public BpmnModelInstance buildFlowWithConditionExpression(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .serviceTask()
                .camundaDelegateExpression("#{twoDelegate}")
                .sequenceFlowId("aaa")
                //.condition("conditionName", "${a==1 || a==2}")
                .condition("conditionName", "${a > b}")
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }

    public BpmnModelInstance buildReceiverFlow(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .receiveTask("receiverTask-id")
                .camundaInputParameter(TASK_IDENTIFIER, "4")
                .message(WAITING_FOR_SPECIFIC_MESSAGE)
                .camundaExecutionListenerDelegateExpression("end", "#{receiveOneExecutionListener}")
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }

    public BpmnModelInstance buildParallelReceiverFlow(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .parallelGateway("fork")
                .receiveTask("receiverTask-id1")
                .camundaInputParameter(TASK_IDENTIFIER, "4")
                .message(WAITING_FOR_SPECIFIC_MESSAGE)
                .camundaExecutionListenerDelegateExpression("end", "#{receiveOneExecutionListener}")
                .parallelGateway("join")
                .moveToNode("fork")
                .receiveTask("receiverTask-id2")
                .camundaInputParameter(TASK_IDENTIFIER, "8")
                .message(WAITING_FOR_SPECIFIC_MESSAGE)
                .camundaExecutionListenerDelegateExpression("end", "#{receiveTwoExecutionListener}")
                .connectTo("join")
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }

    private BpmnModelInstance buildFlowWithServiceTask(String name) {
        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("Initiation received")
                .serviceTask()
                .name("Initiate Payment")
                .camundaClass("com.oshaked.camunda.service.executer.InitiatePaymentDelegate")
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }

    /**
     * global exception
     *
     * @param name
     * @return
     */
    public BpmnModelInstance buildFlowWithTxnGlobalErrorHandler(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .inclusiveGateway("fork")
                .subProcess("sb0")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask("oneDelegate")
                .name("oneDelegate")
                .camundaDelegateExpression("#{oneDelegate}")
                .serviceTask()
                .name("twoDelegate12")
                .camundaDelegateExpression("#{twoDelegate}")
                .endEvent()
                .subProcessDone()
                .inclusiveGateway("join")
                .moveToNode("fork")
                .subProcess("sb1")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask()
                .name("twoDelegate21")
                .camundaDelegateExpression("#{twoDelegate}")
                .serviceTask()
                .name("twoDelegate22")
                .camundaDelegateExpression("#{twoDelegate}")
                .endEvent()
                .subProcessDone()
                .connectTo("join")
                .subProcess()
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask()
                .name("threeDelegate")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent()
                .subProcessDone()
                .endEvent()
                .done();

        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));

        Process process = (Process) modelInstance.getModelElementById("parallel-txn-test-option1");
        SubProcess subProcess = ModelFlows.createElement(modelInstance, process, "e_sb", SubProcess.class);
        subProcess.builder().triggerByEvent()
                .camundaAsync()
                .embeddedSubProcess()
                .startEvent().errorEventDefinition().errorEventDefinitionDone()
                .serviceTask()
                .name("error+handler")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent();
        return modelInstance;
    }

    /**
     * Each transaction handle error inside, and all flow continue
     * catch exception on the boundry txn
     *
     * @param name
     * @return
     */
    public BpmnModelInstance buildFlowWithTxnBoundaryEvent(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .inclusiveGateway("fork")
                .subProcess("sb0")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask("oneDelegate")
                .name("oneDelegate")
                .camundaDelegateExpression("#{oneDelegate}")
                .serviceTask()
                .name("twoDelegate12")
                .camundaDelegateExpression("#{twoDelegate}")
                .endEvent()
                .subProcessDone()
                .boundaryEvent("oneDelegateErr1")
                .errorEventDefinition()
                .errorEventDefinitionDone()
                .serviceTask()
                .name("failed5")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent()/*.error("111")*/
                .moveToNode("sb0")
                .inclusiveGateway("join")
                .moveToNode("fork")
                .subProcess("sb1")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask()
                .name("twoDelegate21")
                .camundaDelegateExpression("#{twoDelegate}")
                .serviceTask()
                .name("twoDelegate22")
                .camundaDelegateExpression("#{twoDelegate}")
                .endEvent()
                .subProcessDone()
                .boundaryEvent("oneDelegateErr")
                .errorEventDefinition()
                .errorEventDefinitionDone()
                .serviceTask()
                .name("failed6")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent()/*.error("111")*/
                .moveToNode("sb1")
                .connectTo("join")
                .subProcess()
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask()
                .name("threeDelegate")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent()
                .subProcessDone()
                .endEvent()
                .done();

        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));

        return modelInstance;
    }

    /**
     * Each transaction handle error inside, and all flow continue.
     * event sub process inside the process
     *
     * @param name
     * @return
     */
    public BpmnModelInstance buildFlowWithTxnEventSubProcess(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .inclusiveGateway("fork")
                .subProcess("sb0")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask("oneDelegate")
                .name("oneDelegate")
                .camundaDelegateExpression("#{oneDelegate}")
                .serviceTask()
                .name("twoDelegate12")
                .camundaDelegateExpression("#{twoDelegate}")
                .endEvent()
                .subProcessDone()
                .inclusiveGateway("join")
                .moveToNode("fork")
                .subProcess("sb1")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask()
                .name("twoDelegate21")
                .camundaDelegateExpression("#{twoDelegate}")
                .serviceTask()
                .name("twoDelegate22")
                .camundaDelegateExpression("#{twoDelegate}")
                .endEvent()
                .subProcessDone()
                .connectTo("join")
                .subProcess("sb2")
                .camundaAsyncBefore()
                .embeddedSubProcess()
                .startEvent()
                .serviceTask()
                .name("threeDelegate")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent()
                .subProcessDone()
                .endEvent()
                .done();

        SubProcess subProcess0 = (SubProcess) modelInstance.getModelElementById("sb0");
        SubProcess eventSubProcess0 = ModelFlows.createElement(modelInstance, subProcess0, "e_sb0", SubProcess.class);
        eventSubProcess0.builder().triggerByEvent()
                .camundaAsync()
                .embeddedSubProcess()
                .startEvent().errorEventDefinition().errorEventDefinitionDone()
                .serviceTask()
                .name("errorHandler0")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent();

        SubProcess subProcess1 = (SubProcess) modelInstance.getModelElementById("sb1");
        SubProcess eventSubProcess1 = ModelFlows.createElement(modelInstance, subProcess1, "e_sb1", SubProcess.class);
        eventSubProcess1.builder().triggerByEvent()
                .camundaAsync()
                .embeddedSubProcess()
                .startEvent().errorEventDefinition().errorEventDefinitionDone()
                .serviceTask()
                .name("errorHandler1")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent();

        SubProcess subProcess2 = (SubProcess) modelInstance.getModelElementById("sb2");
        SubProcess eventSubProcess2 = ModelFlows.createElement(modelInstance, subProcess2, "e_sb2", SubProcess.class);
        eventSubProcess2.builder().triggerByEvent()
                .camundaAsync()
                .embeddedSubProcess()
                .startEvent().errorEventDefinition().errorEventDefinitionDone()
                .serviceTask()
                .name("errorHandler2")
                .camundaDelegateExpression("#{threeDelegate}")
                .endEvent();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }

    public BpmnModelInstance buildParallelServiceFlow(String name) {

        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess(name)
                .name(name)
                .startEvent()
                .name("start")
                .parallelGateway("fork")
                .serviceTask()
                .name("oneDelegate")
                .camundaDelegateExpression("#{oneDelegate}")
                .camundaAsyncBefore(true)
                .parallelGateway("join")
                .moveToNode("fork")
                .serviceTask()
                .name("twoDelegate")
                .camundaDelegateExpression("#{twoDelegate}")
                .camundaAsyncBefore(true)
                .connectTo("join")
                .serviceTask()
                .name("threeDelegate")
                .camundaDelegateExpression("#{threeDelegate}")
                .camundaAsyncBefore(true)
                .endEvent()
                .done();
        Bpmn.validateModel(modelInstance);
        System.out.println(Bpmn.convertToString(modelInstance));
        return modelInstance;
    }
}
