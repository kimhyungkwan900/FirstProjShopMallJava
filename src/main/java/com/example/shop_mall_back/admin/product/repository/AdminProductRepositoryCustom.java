package com.example.shop_mall_back.admin.product.repository;

import com.example.shop_mall_back.admin.product.dto.ProductSearchDto;
import com.example.shop_mall_back.common.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminProductRepositoryCustom {
    Page<Product> getProductPageByCondition(ProductSearchDto productSearchDto, Pageable pageable);
}
