package com.oshaked.camunda.service.we.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println(execution.getCurrentActivityName() +"execution finished");
        logic(execution);
    }

    protected abstract void logic(DelegateExecution execution);
}
