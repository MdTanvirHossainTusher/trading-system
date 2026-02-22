package com.doin.execution.service;

import com.doin.execution.model.Order;
import com.doin.execution.payload.dto.OrderDto;
import com.doin.execution.payload.dto.ParsedSignal;
import org.springframework.data.domain.Page;


public interface OrderService {
    Order createOrder(String orderId, Long userId, Long brokerAccountId, ParsedSignal signal, String rawSignal);
    Page<OrderDto> getOrdersByUser(Long userId, int page, int size);
    OrderDto getOrderByIdForUser(String id, Long userId);
}