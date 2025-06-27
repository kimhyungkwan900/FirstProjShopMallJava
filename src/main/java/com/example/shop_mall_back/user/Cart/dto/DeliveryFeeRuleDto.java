package com.example.shop_mall_back.user.Cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryFeeRuleDto {

    private Long id;                // 정책 ID
    private Integer minOrderAmount; // 최소 주문 금액
    private Integer deliveryFee;    // 배송비
    private String description;     // 설명
}
