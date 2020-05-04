package com.oshaked.camunda.conf;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaBaseConfig {
    public static final int DEFAULT_CONCURRENCY = 1;
    private String bootstrapServers;
    private String appName;

    public KafkaBaseConfig(String bootstrapServers, String appName) {
        this.bootstrapServers = bootstrapServers;
        this.appName = appName;
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    protected <T>ProducerFactory<String, T> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    protected <T>KafkaTemplate<String, T> createProducerTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private Map<String, Object> consumerConfigs(String groupName) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, appName + "#" + groupName);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
        //    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 35000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, NewJsonDeserializer.class);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, groupName); //probably not what we wanted that to do
        return props;
    }

    protected <T>ConsumerFactory<String, T> consumerFactory(Class<T> targetType, String groupName) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(groupName),
                new StringDeserializer(), new JsonDeserializer<>(targetType));
    }

    protected  <T>ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainer(Class<T> targetType, String groupName) {
        return createKafkaListenerContainer(targetType, groupName, DEFAULT_CONCURRENCY);
    }

    protected  <T>ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainer(Class<T> targetType, String groupName, int concurrency) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(targetType, groupName));
        factory.setConcurrency(concurrency);
        return factory;
    }
}
