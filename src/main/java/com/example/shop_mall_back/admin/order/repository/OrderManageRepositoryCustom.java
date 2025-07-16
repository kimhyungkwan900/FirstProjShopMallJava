package com.example.shop_mall_back.admin.order.repository;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.admin.order.dto.OrderSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderManageRepositoryCustom {
    Page<OrderManage> getOrderPageByCondition(OrderSearchDto orderSearchDto, Pageable pageable);

}
