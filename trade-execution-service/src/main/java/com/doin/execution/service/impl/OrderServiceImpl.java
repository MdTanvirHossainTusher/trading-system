package com.doin.execution.service.impl;

import com.doin.execution.model.Order;
import com.doin.execution.payload.dto.OrderUpdateEvent;
import com.doin.execution.payload.dto.ParsedSignal;
import com.doin.execution.repository.OrderRepository;
import com.doin.execution.service.MockBrokerClient;
import com.doin.execution.service.OrderService;
import com.doin.execution.service.OrderUpdatePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderUpdatePublisher orderUpdatePublisher;
    private final MockBrokerClient mockBrokerClient;

    @Value("${broker.execution-delay-ms:5000}")
    private long executionDelayMs;

    @Transactional
    public Order createOrder(String orderId, Long userId, Long brokerAccountId,
                             ParsedSignal signal, String rawSignal) {

        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .brokerAccountId(brokerAccountId)
                .instrument(signal.getInstrument())
                .action(signal.getAction())
                .entryPrice(signal.getEntryPrice())
                .stopLoss(signal.getStopLoss())
                .takeProfit(signal.getTakeProfit())
                .status("PENDING")
                .rawSignal(rawSignal)
                .build();

        orderRepository.save(order);
        log.info("Order [{}] created in PENDING state for user {}", orderId, userId);

        publishUpdate(order, "order.pending");
        simulateOrderLifecycleAsync(order, signal, brokerAccountId.toString());

        return order;
    }

    @Async
    public void simulateOrderLifecycleAsync(Order order, ParsedSignal signal, String accountId) {
        try {
            Thread.sleep(executionDelayMs);

            String brokerOrderId = mockBrokerClient.executeOrder(order.getUserId(), accountId, signal);

            updateOrderStatus(order.getId(), "EXECUTED", brokerOrderId, null, null);

            log.info("Order [{}] transitioned to EXECUTED", order.getId());

            Thread.sleep(executionDelayMs * 2);

            BigDecimal closedPrice = simulateClosePrice(signal);
            BigDecimal pnl = calculatePnl(signal, closedPrice);

            updateOrderStatus(order.getId(), "CLOSED", brokerOrderId, closedPrice, pnl);
            log.info("Order [{}] transitioned to CLOSED. PnL: {}", order.getId(), pnl);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Order lifecycle simulation interrupted for order [{}]", order.getId());
        }
        catch (Exception e) {
            log.error("Error during order lifecycle for [{}]: {}", order.getId(), e.getMessage());
            updateOrderStatus(order.getId(), "FAILED", null, null, null);
        }
    }

    @Transactional
    public void updateOrderStatus(String orderId, String status, String brokerOrderId,
                                  BigDecimal closedPrice, BigDecimal pnl) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            if (brokerOrderId != null) order.setBrokerOrderId(brokerOrderId);
            if (closedPrice != null) order.setClosedPrice(closedPrice);
            if (pnl != null) order.setPnl(pnl);
            order.setUpdatedAt(LocalDateTime.now());
            Order saved = orderRepository.save(order);
            publishUpdate(saved, "order." + status.toLowerCase());
        });
    }

    private BigDecimal simulateClosePrice(ParsedSignal signal) {
        boolean win = Math.random() > 0.4;
        if ("BUY".equals(signal.getAction())) {
            return win ? signal.getTakeProfit() : signal.getStopLoss();
        } else {
            return win ? signal.getTakeProfit() : signal.getStopLoss();
        }
    }

    private BigDecimal calculatePnl(ParsedSignal signal, BigDecimal closedPrice) {
        BigDecimal reference = signal.getEntryPrice() != null
                ? signal.getEntryPrice()
                : signal.getStopLoss().add(signal.getTakeProfit()).divide(new BigDecimal("2"), 6, RoundingMode.HALF_UP);

        BigDecimal diff = closedPrice.subtract(reference);
        if ("SELL".equals(signal.getAction())) diff = diff.negate();

        return diff.multiply(new BigDecimal("10000")).setScale(2, RoundingMode.HALF_UP);
    }

    private void publishUpdate(Order order, String type) {
        OrderUpdateEvent event = OrderUpdateEvent.builder()
                .type(type)
                .orderId(order.getId())
                .userId(order.getUserId())
                .instrument(order.getInstrument())
                .action(order.getAction())
                .entryPrice(order.getEntryPrice())
                .stopLoss(order.getStopLoss())
                .takeProfit(order.getTakeProfit())
                .status(order.getStatus())
                .brokerOrderId(order.getBrokerOrderId())
                .timestamp(LocalDateTime.now().toString())
                .build();
        orderUpdatePublisher.publish(event);
    }


}
