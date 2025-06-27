package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.user.product.domain.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockStatusRepository extends JpaRepository<StockStatus, Long> {
    Optional<StockStatus> findByProduct(Product product);
}
