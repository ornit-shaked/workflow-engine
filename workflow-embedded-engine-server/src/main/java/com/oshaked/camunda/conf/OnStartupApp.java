package com.oshaked.camunda.conf;

import com.oshaked.camunda.misc.Flows;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class OnStartupApp implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    public BpmnModelInstance modelInstance2;
    public Definitions definitions;
    public Process process;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //createReceiverFlow();
        BpmnModelInstance modelInstance = Flows.getParallelReceiverFlow("parallel-receiver-test");
        String key = deploy(modelInstance, "parallel-receiver-test.bpmn");
        createInstance(key);
        createInstance(key);
    }


    private void createReceiverFlow() {

        BpmnModelInstance modelInstance = Flows.getReceiverFlow("receiver-test");

        String key = deploy(modelInstance, "receiver-test.bpmn");
        createInstance(key);
    }

    private void createInstance(String key) {
        runtimeService.startProcessInstanceByKey(key);
    }

    private String deploy(BpmnModelInstance modelInstance, String name) {
        DeploymentWithDefinitions deployment = repositoryService
                .createDeployment()
                .addModelInstance(name, modelInstance)
                .deployWithResult();

        return deployment.getDeployedProcessDefinitions().get(0).getKey();
    }

    private void createFlow1() {
        BpmnModelInstance modelInstance = Bpmn.createExecutableProcess("initiate")
                .name("initiate name")
                .startEvent()
                .name("Initiation received")
                .serviceTask()
                .name("Initiate Payment")
                .camundaClass("com.oshaked.camunda.service.executer.InitiatePaymentDelegate")
                .endEvent()
                .done();

        Bpmn.validateModel(modelInstance);

        Deployment deployment = repositoryService
                .createDeployment()
                .addModelInstance("NewProcess.bpmn", modelInstance).deploy();

        System.out.println("process 1 deployed! " + deployment.getId());
    }

    private void createFlow2() {

        modelInstance2 = Bpmn.createEmptyModel();
        definitions = modelInstance2.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://bpmn.io/schema/bpmn");
        modelInstance2.setDefinitions(definitions);

        // create process
        Process process = createElement(definitions, "initiate2", Process.class);

        // create elements
        StartEvent startEvent = createElement(process, "start", StartEvent.class);
        ParallelGateway fork = createElement(process, "fork", ParallelGateway.class);
        UserTask task1 = createElement(process, "task1", UserTask.class);
        ServiceTask task2 = createElement(process, "task2", ServiceTask.class);
        ParallelGateway join = createElement(process, "join", ParallelGateway.class);
        EndEvent endEvent = createElement(process, "end", EndEvent.class);

        // create flows
        createSequenceFlow(process, startEvent, fork);
        createSequenceFlow(process, fork, task1);
        createSequenceFlow(process, fork, task2);
        createSequenceFlow(process, task1, join);
        createSequenceFlow(process, task2, join);
        createSequenceFlow(process, join, fork);
        createSequenceFlow(process, join, endEvent);

        Bpmn.validateModel(modelInstance2);

        Deployment deployment = repositoryService
                .createDeployment()
                .addModelInstance("initiate2.bpmn", modelInstance2).deployWithResult();
        System.out.println("process 2 deployed! " + deployment.getId());
    }

    protected <T extends BpmnModelElementInstance> T createElement(BpmnModelElementInstance parentElement, String id, Class<T> elementClass) {
        T element = modelInstance2.newInstance(elementClass);
        element.setAttributeValue("id", id, true);
        parentElement.addChildElement(element);
        return element;
    }

    public SequenceFlow createSequenceFlow(Process process, FlowNode from, FlowNode to) {
        SequenceFlow sequenceFlow = createElement(process, from.getId() + "-" + to.getId(), SequenceFlow.class);
        process.addChildElement(sequenceFlow);
        sequenceFlow.setSource(from);
        from.getOutgoing().add(sequenceFlow);
        sequenceFlow.setTarget(to);
        to.getIncoming().add(sequenceFlow);
        return sequenceFlow;
    }

}