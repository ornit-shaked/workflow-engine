//package com.oshaked.camunda.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.camunda.bpm.engine.HistoryService;
//import org.camunda.bpm.engine.RepositoryService;
//import org.camunda.bpm.engine.RuntimeService;
//import org.camunda.bpm.engine.history.HistoricActivityInstance;
//import org.camunda.bpm.engine.runtime.ProcessInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Service
//public class TaskInformationService {
//
//    @Autowired
//    RuntimeService runtimeService;
//
//    @Autowired
//    HistoryService historyService;
//
//    @Autowired
//    RepositoryService repositoryService;
//
//    /**
//     * @return processInstance
//     */
//    public List<HistoricActivityInstance> getTasksNumberByKey(String processInstanceId) {
//
//        log.debug("Trying to start process with processInstanceId'{}'", processInstanceId);
//
//        List<HistoricActivityInstance> historicActivityInstance = historyService.createHistoricActivityInstanceQuery()
//                .activityType("serviceTask")
//                .processInstanceId(processInstanceId)
//                .finished()
//                .orderByHistoricActivityInstanceEndTime().desc()
//                .listPage(0, 10);
//
//        repositoryService.getBpmnModelInstance(key).getModelElementsByType(ServiceTask);
//
//        if (historicActivityInstance == null) {
//            throw new RuntimeException("Cannot retrieve history for  key: " + processInstanceId);
//        }
//
//        log.debug("Retrieve history for key '{}' finished successfully", processInstanceId);
//
//        return historicActivityInstance;
//    }
//}
