package com.example.shop_mall_back.user.product.repository;

import com.example.shop_mall_back.user.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductIdOrderByIdAsc(Long productId);

    ProductImage findByProductIdAndIsRepImgTrue(Long productId);
}