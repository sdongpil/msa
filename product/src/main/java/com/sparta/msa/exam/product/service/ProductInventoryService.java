package com.sparta.msa.exam.product.service;

import com.sparta.msa.exam.product.domain.entity.Product;
import com.sparta.msa.exam.product.domain.repository.ProductRepository;
import com.sparta.msa.exam.product.dto.StockReservationRequestDto;
import com.sparta.msa.exam.product.exception.ErrorCode;
import com.sparta.msa.exam.product.exception.ProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.sparta.msa.exam.product.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.sparta.msa.exam.product.exception.ErrorCode.STOCK_RESERVATION_NOT_FOUND;


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

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        if (product.getStock() < quantity) {
            return false;
        }

        String key = STOCK_RESERVATION + transactionId;
        redisTemplate.opsForHash().put(key, id.toString(), String.valueOf(quantity));

        return true;
    }


    @Transactional
    public boolean commitStockReservation(String transactionId) {
        String key = STOCK_RESERVATION + transactionId;

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            throw new ProductException(STOCK_RESERVATION_NOT_FOUND);
        }

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            int quantity = Integer.parseInt(entry.getValue().toString());

            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
            product.decrease(quantity);
        }

        return true;
    }

    @Transactional
    public void rollbackCommittedStock(String transactionId) {
        String key = STOCK_RESERVATION + transactionId;

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) {
            throw new ProductException(STOCK_RESERVATION_NOT_FOUND);
        }

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            Long productId = Long.valueOf(entry.getKey().toString());
            int quantity = Integer.parseInt(entry.getValue().toString());

            Product product = productRepository.findById(productId).orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
            product.increase(quantity);
        }
    }

    public void deleteStockReservation(String transactionId) {
        String key = STOCK_RESERVATION + transactionId;
        redisTemplate.delete(key);
    }
}
