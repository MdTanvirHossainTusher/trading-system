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
public class AnalyticsDto {
    private long totalOrders;
    private long pendingOrders;
    private long executedOrders;
    private long closedOrders;
    private long failedOrders;
    private BigDecimal totalPnl;
    private double winRate;
    private long winningTrades;
    private long losingTrades;
}