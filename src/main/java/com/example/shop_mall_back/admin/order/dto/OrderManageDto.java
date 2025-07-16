package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderManageDto {

    private Long orderManageId;

    private OrderManage.OrderStatus orderStatus;

    private AdminOrderDto order;
}
