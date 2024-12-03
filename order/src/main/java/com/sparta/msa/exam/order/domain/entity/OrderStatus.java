package com.sparta.msa.exam.order.domain.entity;

public enum OrderStatus {
    SUCCESS("주문 완료"),
    FAIL("주문 실패"),
    SHIPPED("발송"),
    DELIVERED("배송 완료"),
    CANCELLED("주문 취소"),
    PENDING("주문 대기");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
