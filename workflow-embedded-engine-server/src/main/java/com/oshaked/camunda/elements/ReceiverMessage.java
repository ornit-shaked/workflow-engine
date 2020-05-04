package com.oshaked.camunda.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiverMessage {

    private String messageId = UUID.randomUUID().toString();
    private String correlationId;
    private String identity;
}
