package com.oshaked.camunda.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class NewJsonDeserializer<T> extends JsonDeserializer<T> {
    public NewJsonDeserializer() {
    }

    public NewJsonDeserializer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    public NewJsonDeserializer(Class<T> targetType) {
        super(targetType);
    }

    public NewJsonDeserializer(Class<T> targetType, ObjectMapper objectMapper) {
        super(targetType, objectMapper);
    }

    public T deserialize(String topic, byte[] data) {
        try {
            return super.deserialize(topic, data);
        } catch (SerializationException var4) {
            return null;
        }
    }
}
