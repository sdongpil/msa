package com.sparta.msa.exam.order.domain.repository;

import com.sparta.msa.exam.order.domain.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);
}
