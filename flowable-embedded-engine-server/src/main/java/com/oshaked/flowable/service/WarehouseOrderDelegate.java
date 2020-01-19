package com.oshaked.flowable.service;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class WarehouseOrderDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        System.out.println("WarehouseOrderDelegate execution "  +execution.getProcessInstanceId());
        Integer items = (Integer) execution.getVariable("items");

        boolean isSuccess = (items > 99 )? false :  true;

        // Complete the task
        execution.setVariable("success", isSuccess);

    }
}
