package com.example.shop_mall_back.admin.product;

import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.domain.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImgRepository extends JpaRepository<ProductImg,Long> {
}
