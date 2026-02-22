package com.doin.execution.service;

import com.doin.execution.payload.dto.OrderUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderUpdatePublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.order-updates}")
    private String orderUpdatesTopic;

    public void publish(OrderUpdateEvent event) {
        kafkaTemplate.send(orderUpdatesTopic, event.getOrderId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish order update for order {}: {}", event.getOrderId(), ex.getMessage());
                    } else {
                        log.debug("Order update published: {} -> {}", event.getOrderId(), event.getType());
                    }
                });
    }
}