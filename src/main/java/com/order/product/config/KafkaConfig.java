package com.order.product.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic checkSendOrderTopic() {
        return new NewTopic("send-email-order",2,(short) 1);
    }

    @Bean
    NewTopic checkSendOrderMessageTopic() {
        return new NewTopic("send-email-order-message",2,(short) 1);
    }
}
