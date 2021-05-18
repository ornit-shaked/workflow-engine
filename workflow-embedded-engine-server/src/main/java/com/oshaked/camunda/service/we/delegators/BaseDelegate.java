package com.oshaked.camunda.service.we.delegators;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public abstract class BaseDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println(execution.getCurrentActivityName() + " execution started");
        logic(execution);
        System.out.println(execution.getCurrentActivityName() + " execution done");
    }

    protected abstract void logic(DelegateExecution execution) throws Exception;


}