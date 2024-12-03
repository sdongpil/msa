package com.sparta.msa.exam.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparta.msa.exam.product.domain.ProductStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductResponseDto(Long id,
                                 String name,
                                 int supplyPrice,
                                 ProductStatus productStatus,
                                 int stock,
                                 @JsonSerialize(using = LocalDateTimeSerializer.class)
                                 @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                                 @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime createdAt
) {
}

