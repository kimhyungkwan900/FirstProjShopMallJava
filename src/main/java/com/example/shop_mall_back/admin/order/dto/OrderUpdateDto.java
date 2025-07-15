package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.admin.order.domain.OrderManage;
import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateDto {
    private List<Long> ids;
    private OrderManage.OrderStatus newStatus;
}

