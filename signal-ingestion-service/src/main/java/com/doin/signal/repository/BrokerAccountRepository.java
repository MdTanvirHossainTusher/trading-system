package com.doin.signal.repository;

import com.doin.signal.model.BrokerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrokerAccountRepository extends JpaRepository<BrokerAccount, Long> {
    Optional<BrokerAccount> findByUserIdAndActiveTrue(Long userId);
    List<BrokerAccount> findByUserId(Long userId);
}
