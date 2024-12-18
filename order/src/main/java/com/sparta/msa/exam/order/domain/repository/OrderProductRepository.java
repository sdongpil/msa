package com.sparta.msa.exam.order.domain.repository;

import com.sparta.msa.exam.order.domain.entity.order.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(Long orderId);
}
