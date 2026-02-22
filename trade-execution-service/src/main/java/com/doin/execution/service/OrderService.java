package com.doin.execution.service;

import com.doin.execution.model.Order;
import com.doin.execution.payload.dto.ParsedSignal;


public interface OrderService {
    Order createOrder(String orderId, Long userId, Long brokerAccountId, ParsedSignal signal, String rawSignal);
}