package com.example.shop_mall_back.user.Cart.dto;

import com.example.shop_mall_back.user.Cart.domain.DeliveryFeeRule;
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

    //서비스에서 DeliveryFeeRule을 조회해서 DTO로 변환할 때 사용
    public DeliveryFeeRuleDto convertToDto(DeliveryFeeRule rule) {
        return DeliveryFeeRuleDto.builder()
                .id(rule.getId())
                .minOrderAmount(rule.getMinOrderAmount())
                .deliveryFee(rule.getDeliveryFee())
                .description(rule.getDescription())
                .build();
    }
}
