package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import com.example.shop_mall_back.admin.product.dto.ProductSearchDto;
import com.example.shop_mall_back.common.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderManageRepositoryCustom {
    Page<OrderManage> getOrderPageByCondition(OrderSearchDto orderSearchDto, Pageable pageable);
}
