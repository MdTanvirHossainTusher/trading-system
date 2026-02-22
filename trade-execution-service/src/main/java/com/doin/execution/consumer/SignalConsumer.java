package com.doin.execution.consumer;

import com.doin.execution.exception.SignalParseException;
import com.doin.execution.payload.dto.ParsedSignal;
import com.doin.execution.payload.dto.RawSignalEvent;
import com.doin.execution.service.OrderService;
import com.doin.execution.util.SignalParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignalConsumer {

    private final SignalParser signalParser;
    private final OrderService orderService;

    @KafkaListener(
            topics = "${kafka.topics.raw-signals}",
            groupId = "trade-execution-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeSignal(
            @Payload RawSignalEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("Consuming signal [{}] from user {} | partition={} offset={}",
                event.getSignalId(), event.getUserId(), partition, offset);

        try {
            ParsedSignal parsed = signalParser.parse(event.getRawMessage());

            orderService.createOrder(
                    event.getSignalId(),
                    event.getUserId(),
                    event.getBrokerAccountId(),
                    parsed,
                    event.getRawMessage()
            );
            log.info("Signal [{}] successfully processed {} {}", event.getSignalId(), parsed.getAction(), parsed.getInstrument());
        }
        catch (SignalParseException e) {
            log.warn("Signal [{}] rejected due to parse error: {}", event.getSignalId(), e.getMessage());
//            throw new SignalParseException("Signal " + event.getSignalId() + " rejected due to parse error: {" + e.getMessage());
        }
        catch (Exception e) {
            log.error("Unexpected error processing signal [{}]: {}", event.getSignalId(), e.getMessage(), e);
//            throw new SignalParseException("Unexpected error processing signal " + event.getSignalId() +
//                    ": " + e.getMessage());

        }
    }
}