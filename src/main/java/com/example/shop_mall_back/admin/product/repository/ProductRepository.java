package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.common.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {


}
