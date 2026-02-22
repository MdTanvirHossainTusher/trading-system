package com.doin.execution.repository;

import com.doin.execution.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Optional<Order> findByIdAndUserId(String id, Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(Long userId, String status);

    @Query("SELECT COALESCE(SUM(o.pnl), 0) FROM Order o WHERE o.userId = :userId AND o.status = 'CLOSED'")
    BigDecimal sumPnlByUserId(Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId AND o.status = 'CLOSED' AND o.pnl > 0")
    long countWinningTradesByUserId(Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.userId = :userId AND o.status = 'CLOSED' AND o.pnl <= 0")
    long countLosingTradesByUserId(Long userId);
}