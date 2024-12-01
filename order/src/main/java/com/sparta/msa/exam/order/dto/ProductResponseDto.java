package com.sparta.msa.exam.order.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductResponseDto(Long id,
                                 String name,
                                 int supplyPrice,
                                 String productStatus,
                                 int stock,
                                 LocalDateTime createdAt
) {
}

