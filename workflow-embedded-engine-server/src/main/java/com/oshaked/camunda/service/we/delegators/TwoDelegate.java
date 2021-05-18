package com.oshaked.camunda.service.we.delegators;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwoDelegate extends BaseDelegate {

    @Override
    protected void logic(DelegateExecution execution) {
        //throw new BpmnError("my_error");

    }
}