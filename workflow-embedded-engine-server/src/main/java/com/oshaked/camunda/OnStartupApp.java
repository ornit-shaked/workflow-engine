package com.oshaked.camunda;

import com.oshaked.camunda.misc.FluentFlows;
import com.oshaked.camunda.service.DeploymentService;
import com.oshaked.camunda.service.ProcessInstanceService;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OnStartupApp implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private  DeploymentService deploymentService;

    @Autowired
    private ProcessInstanceService processService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FlowWithConditionExpression();
        //createReceiverFlow();
        //createParallelReceiverFlow();
        //createParallelServiceFlow();
        //createFlowWithTxn();
    }

    private void FlowWithConditionExpression() {

        Map<String, Object> input  = new HashMap<>();
        input.put("a", new String("2"));
        input.put("b", new String("1"));
        BpmnModelInstance modelInstance = FluentFlows.buildFlowWithConditionExpression("condition-expression");
        String key = deploymentService.createDeployment(modelInstance, "condition-expression.bpmn");
        processService.startProcessInstanceByKey(key, input);
    }

    private void createReceiverFlow() {

        BpmnModelInstance modelInstance = FluentFlows.buildReceiverFlow("receiver-test");
        String key = deploymentService.createDeployment(modelInstance, "receiver-test.bpmn");
        processService.startProcessInstanceByKey(key, null);
    }

    private void createParallelReceiverFlow() {

        BpmnModelInstance modelInstance = FluentFlows.buildParallelReceiverFlow("parallel-receiver-test");
        String key = deploymentService.createDeployment(modelInstance, "parallel-receiver-test.bpmn");

        //create 2 instances of same proccess
        processService.startProcessInstanceByKey(key, null);
        processService.startProcessInstanceByKey(key, null);
    }

    private void createParallelServiceFlow() {

        BpmnModelInstance modelInstance = FluentFlows.buildParallelServiceFlow("parallel-service-task-test");
        String key = deploymentService.createDeployment(modelInstance, "parallel-service-task-test.bpmn");
        System.out.println("Workflow deployed: "+key);
        //processService.startProcessInstanceByKey(key, null);
    }

    private void createFlowWithTxn() {
        BpmnModelInstance modelInstance1 = FluentFlows.buildFlowWithTxnGlobalErrorHandler("parallel-txn-test-option1");
        String key1 = deploymentService.createDeployment(modelInstance1, "parallel-txn-test-option1.bpmn");
        System.out.println("Workflow deployed option 1: "+key1);

        BpmnModelInstance modelInstance2 = FluentFlows.buildFlowWithTxnBoundaryEvent("parallel-txn-test-option2");
        String key2 = deploymentService.createDeployment(modelInstance2, "parallel-txn-test-option2.bpmn");
        System.out.println("Workflow deployed option 2: "+key2);

        //look like uneccesary
        BpmnModelInstance modelInstance3 = FluentFlows.buildFlowWithTxnEventSubProcess("parallel-txn-test-option3");
        String key3 = deploymentService.createDeployment(modelInstance3, "parallel-txn-test-option3.bpmn");
        System.out.println("Workflow deployed option 3: "+key3);
    }



}