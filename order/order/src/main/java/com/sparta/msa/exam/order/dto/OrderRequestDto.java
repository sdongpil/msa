package com.sparta.msa.exam.order.dto;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public record OrderRequestDto (
        List<Long> productList,
        Long userId
        ) {
}
