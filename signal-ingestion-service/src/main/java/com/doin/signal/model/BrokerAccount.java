package com.doin.signal.model;

import com.doin.signal.constant.db.DbConstant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = DbConstant.DbBrokerAccount.TABLE_NAME)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "api_key_hash", nullable = false)
    private String apiKeyHash;

    @Column(name = "api_key_last4")
    private String apiKeyLast4;

    @Column(name = "broker_type")
    @Builder.Default
    private String brokerType = "METATRADER";

    @Column
    @Builder.Default
    private Boolean active = true;

    @Column(name = DbConstant.DbCommon.CREATED_AT)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
