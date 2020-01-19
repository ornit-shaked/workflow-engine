package com.ecitele.camunda.web.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InitiatePaymentDto {
    Integer price;
    Integer items;
    ArrayList<Integer> arrParallel = new ArrayList<>();
    ArrayList<Integer> arrSequence = new ArrayList<>();
}
