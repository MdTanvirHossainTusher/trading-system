package com.doin.execution.repository;

import com.doin.execution.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findByUserId(Long userId, Pageable pageable);
    Optional<Order> findByIdAndUserId(String id, Long userId);

}