package com.doin.execution.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "broker_account_id")
    private Long brokerAccountId;

    @Column(nullable = false, length = 20)
    private String instrument;

    @Column(nullable = false, length = 10)
    private String action;  // BUY | SELL

    @Column(name = "entry_price", precision = 18, scale = 6)
    private BigDecimal entryPrice;

    @Column(name = "stop_loss", precision = 18, scale = 6)
    private BigDecimal stopLoss;

    @Column(name = "take_profit", precision = 18, scale = 6)
    private BigDecimal takeProfit;

    @Column(length = 20)
    @Builder.Default
    private String status = "PENDING";  // PENDING | EXECUTED | CLOSED | FAILED

    @Column(name = "raw_signal", columnDefinition = "TEXT")
    private String rawSignal;

    @Column(name = "broker_order_id")
    private String brokerOrderId;

    @Column(name = "closed_price", precision = 18, scale = 6)
    private BigDecimal closedPrice;

    @Column(name = "pnl", precision = 18, scale = 6)
    private BigDecimal pnl;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
