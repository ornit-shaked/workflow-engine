package com.oshaked.camunda.conf;

import com.oshaked.camunda.elements.ReceiverMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Profile("kafka")
@Configuration
public class KafkaConsumerConfig extends KafkaBaseConfig{

    public static final String RECEIVER_MESSAGE ="receiverMessage";


    public KafkaConsumerConfig(@Value("${kafka.address}") String bootstrapServers, @Value("${spring.application.name}") String appName) {
        super(bootstrapServers, appName);
    }


    @Bean(name = RECEIVER_MESSAGE)
    public ConcurrentKafkaListenerContainerFactory<String, ReceiverMessage> deleteHostAnnouncement() {
        return createKafkaListenerContainer(ReceiverMessage.class, "dh_announcement");
    }

}
