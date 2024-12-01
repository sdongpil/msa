package com.sparta.msa.exam.order.domain.repository;

import com.sparta.msa.exam.order.domain.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
