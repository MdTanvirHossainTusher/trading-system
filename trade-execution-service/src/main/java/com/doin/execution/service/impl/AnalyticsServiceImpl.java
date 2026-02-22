package com.doin.execution.service.impl;

import com.doin.execution.payload.dto.AnalyticsDto;
import com.doin.execution.repository.OrderRepository;
import com.doin.execution.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;

    public AnalyticsDto getAnalytics(Long userId) {
        long total = orderRepository.countByUserId(userId);
        long pending = orderRepository.countByUserIdAndStatus(userId, "PENDING");
        long executed = orderRepository.countByUserIdAndStatus(userId, "EXECUTED");
        long closed = orderRepository.countByUserIdAndStatus(userId, "CLOSED");
        long failed = orderRepository.countByUserIdAndStatus(userId, "FAILED");
        BigDecimal totalPnl = orderRepository.sumPnlByUserId(userId);
        long winning = orderRepository.countWinningTradesByUserId(userId);
        long losing = orderRepository.countLosingTradesByUserId(userId);
        double winRate = closed > 0 ? (double) winning / closed * 100 : 0.0;

        return AnalyticsDto.builder()
                .totalOrders(total)
                .pendingOrders(pending)
                .executedOrders(executed)
                .closedOrders(closed)
                .failedOrders(failed)
                .totalPnl(totalPnl)
                .winRate(Math.round(winRate * 100.0) / 100.0)
                .winningTrades(winning)
                .losingTrades(losing)
                .build();
    }
}
