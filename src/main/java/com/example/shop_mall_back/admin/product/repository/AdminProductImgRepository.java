package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.user.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminProductImgRepository extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductIdOrderByIdAsc(Long productId);
}
