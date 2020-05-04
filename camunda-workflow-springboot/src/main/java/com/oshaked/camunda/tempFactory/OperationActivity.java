package com.oshaked.camunda.tempFactory;

import com.oshaked.camunda.service.WfExecutionUtil;
import com.oshaked.camunda.tempmodel.properties.ActivityTypes;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.springframework.stereotype.Component;

@Component
public class OperationActivity implements Activity {


    @Override
    public <T extends BpmnModelElementInstance> T buildInternalFlow(BpmnModelInstance modelInstance,
                                                                    BpmnModelElementInstance parentElement,
                                                                    String nodeInstanceId) {
        System.out.println("operation activity");
        ServiceTask task = WfExecutionUtil
                .createElement(modelInstance, parentElement, ServiceTask.class,  "as_" /*+ getUrl()*/);
        task.setCamundaDelegateExpression("${testDelegate}");

//        //todo
//        NodeTemplateExtension nodeTemplate = new NodeTemplateExtension(url, nodeInstanceId, "service");
//        //interfaceName = callOperatopn
//        //ServiceInstanceId = serviceInstancemodel.getIdentifier()
//
//        CamundaProperties properties = NodeTemplateMapper.nodeTemplateToBpmnExtension(modelInstance, nodeTemplate);
//        task.builder().addExtensionElement(properties);

        return (T) task;
    }

    @Override
    public String getObjectType() {
        return ActivityTypes.CALL_OPERATION.getActivityType();
    }

}
