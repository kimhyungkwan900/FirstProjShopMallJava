package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.common.domain.Order;
import com.example.shop_mall_back.user.Order.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderManageDto {

    private Long orderManageId;

    private OrderManage.OrderStatus orderStatus;

    private OrderDto order;
}
