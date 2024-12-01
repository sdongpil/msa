package com.sparta.msa.exam.product.service;

import com.sparta.msa.exam.product.domain.entity.Product;
import com.sparta.msa.exam.product.domain.repository.ProductRepository;
import com.sparta.msa.exam.product.dto.StockReservationRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductInventoryService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final String STOCK_RESERVATION = "product-stock:reserve:";


    @Transactional
    public boolean prepareStockReservation(Long id, StockReservationRequestDto requestDto) {
        int quantity = requestDto.getQuantity();
        String transactionId = requestDto.getTransactionId();

        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));

        if (product.getStock() < quantity) {
            return false;
        }

        String key = STOCK_RESERVATION + transactionId;
        redisTemplate.opsForHash().put(key, id.toString(), String.valueOf(quantity));

        return true;
    }

    public void rollbackStockReservation(String transactionId) {
        String key = STOCK_RESERVATION + transactionId;
        redisTemplate.delete(key);
    }

    @Transactional
    public void commitStockReservation(String transactionId) {
        String key = STOCK_RESERVATION + transactionId;

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            throw new IllegalStateException("재고 예약 정보를 불러올 수 없습니다.");
        }

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            int quantity = Integer.parseInt(entry.getValue().toString());

            Product product = productRepository.findById(productId).orElseThrow();
            product.decrease(quantity);
        }

        redisTemplate.delete(key);
    }
}
