package com.doin.notification.consumer;

import com.doin.notification.config.RawOrderWebSocketHandler;
import com.doin.notification.payload.dto.OrderUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderUpdateConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final RawOrderWebSocketHandler rawOrderWebSocketHandler;

    @KafkaListener(
            topics = "${kafka.topics.order-updates}",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(OrderUpdateEvent event) {
        log.info("Broadcasting order update: {} → {} [{}]",
                event.getOrderId(), event.getType(), event.getStatus());

        // STOMP broadcast (for frontend)
        messagingTemplate.convertAndSend("/topic/orders", event);
        messagingTemplate.convertAndSend("/topic/orders/" + event.getUserId(), event);

        // Raw WebSocket broadcast for Postman testing
        rawOrderWebSocketHandler.broadcast(event);

        log.debug("WebSocket broadcast sent for order [{}]", event.getOrderId());
    }
}