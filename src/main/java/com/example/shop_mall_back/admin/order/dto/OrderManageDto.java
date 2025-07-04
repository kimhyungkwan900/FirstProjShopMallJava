package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.common.domain.Order;
import lombok.Getter;

@Getter
public class OrderManageDto {

    private Long orderManageId;

    private OrderManage.OrderStatus orderStatus;

    private Order order;
}
