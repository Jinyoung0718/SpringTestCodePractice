package com.sjy.dayontest;

import com.sjy.dayontest.service.KafKaConsumerService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafKaConsumerApplication {

    private final KafKaConsumerService kafkaConsumerService;

    @Bean
    public NewTopic topic() {
         return TopicBuilder.name("test-topic")
                 .build();
    }

    @KafkaListener(id = "test-id", topics = "test-topic")
    public void listen(String message) {
        kafkaConsumerService.process(message);
    }
}
