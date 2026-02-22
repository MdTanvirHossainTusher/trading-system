package com.doin.execution.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateEvent {
    private String type;
    private String orderId;
    private Long userId;
    private String instrument;
    private String action;
    private BigDecimal entryPrice;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private String status;
    private String brokerOrderId;
    private String timestamp;
}