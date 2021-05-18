package com.oshaked.camunda.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeploymentService {

    private static final String BPMN_RESOURCE_SUFFIX = ".bpmn20.xml";

    @Autowired
    private RepositoryService repositoryService;

    public String createDeployment(BpmnModelInstance modelInstance, String workflowName) {

        log.debug("Trying to deploy model '{}'", workflowName);

        DeploymentWithDefinitions deployment = repositoryService.createDeployment()
                .addModelInstance(generateResourceName(workflowName), modelInstance)
                .deployWithResult();

        isDeploymentSuccess(workflowName, deployment);

        String key = deployment.getDeployedProcessDefinitions().get(0).getKey();
        log.debug("Model '{}' with name '{}' and key '{}' deployed successfully",
                workflowName, deployment.getName(), key);

        return key;
    }

    
    private void isDeploymentSuccess(String workflowName, DeploymentWithDefinitions deployment) {
        if (deployment == null
                || deployment.getDeployedProcessDefinitions() == null
                || deployment.getDeployedProcessDefinitions().get(0) == null) {
            throw new RuntimeException("Cannot deploy '"+workflowName + "' workflow with workflowName");
        }
    }

    private String generateResourceName(String resourceName) {
        return resourceName + BPMN_RESOURCE_SUFFIX;
    }

}
