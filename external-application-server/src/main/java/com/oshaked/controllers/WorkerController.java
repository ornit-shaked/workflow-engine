package com.oshaked.controllers;

import com.oshaked.service.ChargeCardWorker;
import com.oshaked.service.handler.ShipOrderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"workflow"})
public class WorkerController {

    @Autowired
    ChargeCardWorker chargeCardWorker;


    @GetMapping("/createWorker/{type}")
    public void createWorker(@PathVariable("type") String type) {

        chargeCardWorker.creatrWorker(new ShipOrderHandler(), type);


    }
}
