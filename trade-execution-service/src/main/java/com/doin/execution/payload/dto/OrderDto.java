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
public class OrderDto {
    private String id;
    private Long userId;
    private String instrument;
    private String action;
    private BigDecimal entryPrice;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;
    private String status;
    private String brokerOrderId;
    private BigDecimal closedPrice;
    private BigDecimal pnl;
    private String createdAt;
    private String updatedAt;
}