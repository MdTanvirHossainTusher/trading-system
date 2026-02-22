package com.doin.execution.service.impl;

import com.doin.execution.model.Order;
import com.doin.execution.payload.dto.ParsedSignal;
import com.doin.execution.repository.OrderRepository;
import com.doin.execution.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

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

        return order;
    }



}
