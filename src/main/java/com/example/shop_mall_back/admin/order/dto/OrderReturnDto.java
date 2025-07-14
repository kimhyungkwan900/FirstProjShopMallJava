package com.example.shop_mall_back.admin.order.dto;

import com.example.shop_mall_back.user.myOrder.domain.OrderReturn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderReturnDto {
    private Long id;
    private Long orderId;
    private Long memberId;
    private OrderReturn.ReturnType returnType;
    private String reason;
    private String detail;
}
