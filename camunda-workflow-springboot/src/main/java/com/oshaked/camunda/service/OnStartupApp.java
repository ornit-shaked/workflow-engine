package com.oshaked.camunda.service;

import com.oshaked.camunda.elementsFactory.BpmnElementMapper;
import com.oshaked.camunda.elementsFactory.BpmnElementMapperFactory;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ProcessBuilder;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
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

    public BpmnModelInstance modelInstance;
    public Definitions definitions;
    public Process process;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        BpmnElementMapper a = BpmnElementMapperFactory.getObject(StartEvent.class.getSimpleName());

        createFlow();

//        List<ActivityDefinition> activities = new ArrayList<>();
//        activities.add(new OperationActivityDefinition());
//        activities.add(new StateActivityDefinition());
//
//        LinkedList<BpmnModelElementInstance> subProcessTasks = new LinkedList<>();
//        for (ActivityDefinition activity : activities) {
//            subProcessTasks.add(ActivityFactory.getService(activity.getType().getActivityType())
//                    .buildInternalFlow(modelInstance, process));
//        }
//        System.out.println("done");
    }

    private void buildFlow() {
        // first create the process builder itself
        ProcessBuilder processBuilder = Bpmn.createExecutableProcess("process-payments");

        // parse your file, and
        // if condition, then create a startEvent
        AbstractFlowNodeBuilder builder = processBuilder.startEvent().id("start");

        // if condition, then create a serviceTask
        builder = builder.serviceTask().id("task").name("Process Payment");

        // if condition, then create an endEvent
        builder = builder.endEvent().id("end");

        // at the end, create the model and print / persist it
        BpmnModelInstance process = builder.done();
        System.out.println(Bpmn.convertToString(process));

        Deployment deployment = repositoryService
                .createDeployment().name("modelInstance")
                .addModelInstance("initiate2.bpmn", modelInstance).deployWithResult();
        System.out.println("process 2 deployed! " + deployment.getId());

    }

    private BpmnModelInstance createFlow() {

        modelInstance = Bpmn.createEmptyModel();
        definitions = modelInstance.newInstance(Definitions.class);
        definitions.setTargetNamespace("http://bpmn.io/schema/bpmn");
        definitions.setName("definitionName");
        definitions.setId("definitionId");
        modelInstance.setDefinitions(definitions);

        // create process
        process = createElement(definitions, "initiate2", Process.class);
        process.setExecutable(true);

        StartEvent startEvent = createElement(process, "start", StartEvent.class);
//        ParallelGateway fork = createElement(process, "fork", ParallelGateway.class);
//        ServiceTask task1 = createElement(process, "task1", ServiceTask.class);
//        task1.setCamundaExpression("${InitiatePaymentDelegate}");
//        ServiceTask task2 = createElement(process, "task2", ServiceTask.class);
//        task2.setCamundaExpression("${InitiatePaymentDelegate}");
//        ParallelGateway join = createElement(process, "join", ParallelGateway.class);
        EndEvent endEvent = createElement(process, "end", EndEvent.class);

        // create flows
//        createSequenceFlow(process, startEvent, fork);
//        createSequenceFlow(process, fork, task1);
//        createSequenceFlow(process, fork, task2);
//        createSequenceFlow(process, task1, join);
//        createSequenceFlow(process, join, endEvent);

        Bpmn.validateModel(modelInstance);

//        Deployment deployment = repositoryService
//                .createDeployment().name("modelInstance")
//                .addModelInstance("initiate2.bpmn", modelInstance).deployWithResult();
//        System.out.println("process 2 deployed! " + deployment.getId());
//
//        System.out.println(Bpmn.convertToString(modelInstance));

        //ProcessInstantiationBuilder a = runtimeService.createProcessInstanceByKey("modelInstance");
        //ProcessInstance processInstanceId = runtimeService.startProcessInstanceByKey("modelInstance");
        return modelInstance;
    }

    protected <T extends BpmnModelElementInstance> T createElement(
            BpmnModelElementInstance parentElement,
            String id,
            Class<T> elementClass) {

        T element = modelInstance.newInstance(elementClass);
        element.setAttributeValue("id", id, true);
        parentElement.addChildElement(element);

        BpmnShape bpmnShape = modelInstance.newInstance(BpmnShape.class);
        bpmnShape.setBpmnElement((BaseElement) element);

        return element;
    }

    public SequenceFlow createSequenceFlow(
            Process process,
            FlowNode from,
            FlowNode to) {

        SequenceFlow sequenceFlow = createElement(process, from.getId() + "-" + to.getId(), SequenceFlow.class);
        process.addChildElement(sequenceFlow);
        sequenceFlow.setSource(from);
        from.getOutgoing().add(sequenceFlow);
        sequenceFlow.setTarget(to);
        to.getIncoming().add(sequenceFlow);
        return sequenceFlow;
    }

}