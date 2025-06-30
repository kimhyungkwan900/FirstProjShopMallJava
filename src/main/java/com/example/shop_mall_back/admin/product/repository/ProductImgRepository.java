package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.user.product.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImgRepository extends JpaRepository<ProductImage,Long> {
}
