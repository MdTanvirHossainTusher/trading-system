package com.doin.signal.producer;

import com.doin.signal.payload.dto.RawSignalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignalProducer {

    private final KafkaTemplate<String, RawSignalEvent> kafkaTemplate;

    @Value("${kafka.topics.raw-signals}")
    private String rawSignalsTopic;

    public void publishSignal(RawSignalEvent event) {
        String partitionKey = String.valueOf(event.getUserId());

        CompletableFuture<SendResult<String, RawSignalEvent>> future = kafkaTemplate.send(rawSignalsTopic, partitionKey, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish signal [{}] for user {}: {}",
                        event.getSignalId(), event.getUserId(), ex.getMessage());
            } else {
                log.info("Signal [{}] published to partition {} offset {}",
                        event.getSignalId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}