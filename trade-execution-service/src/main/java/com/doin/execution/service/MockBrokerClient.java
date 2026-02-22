package com.doin.execution.service;

import com.doin.execution.payload.dto.ParsedSignal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class MockBrokerClient {

    public String executeOrder(Long userId, String accountId, ParsedSignal signal) {
        String brokerOrderId = "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        log.info("MOCK BROKER EXECUTION: ");
        log.info("Broker Order ID : {}", brokerOrderId);
        log.info("User ID         : {}", userId);
        log.info("Account ID      : {}", accountId);
        log.info("Action          : {}", signal.getAction());
        log.info("Instrument      : {}", signal.getInstrument());
        log.info("Entry Price     : {}", signal.getEntryPrice() != null ? signal.getEntryPrice() : "MARKET");
        log.info("Stop Loss       : {}", signal.getStopLoss());
        log.info("Take Profit     : {}", signal.getTakeProfit());

        return brokerOrderId;
    }
}