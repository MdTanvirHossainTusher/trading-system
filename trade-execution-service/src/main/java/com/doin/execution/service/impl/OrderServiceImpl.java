package com.doin.execution.service.impl;

import com.doin.execution.model.Order;
import com.doin.execution.payload.dto.OrderUpdateEvent;
import com.doin.execution.payload.dto.ParsedSignal;
import com.doin.execution.repository.OrderRepository;
import com.doin.execution.service.OrderService;
import com.doin.execution.service.OrderUpdatePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderUpdatePublisher orderUpdatePublisher;

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

        return order;
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
