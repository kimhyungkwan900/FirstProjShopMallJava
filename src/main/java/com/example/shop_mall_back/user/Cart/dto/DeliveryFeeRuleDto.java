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
    
    // ✅ 추가
    private int totalProductPrice;     // 선택된 상품 합계
    private int grandTotal;            // 총합 (상품 합계 + 배송비)


    // 서비스에서 DeliveryFeeRule을 조회해서 DTO로 변환할 때 사용
    public static DeliveryFeeRuleDto from(DeliveryFeeRule rule, int totalProductPrice, int grandTotal) {
        return DeliveryFeeRuleDto.builder()
                .id(rule.getId())
                .minOrderAmount(rule.getMinOrderAmount())
                .deliveryFee(rule.getDeliveryFee())
                .description(rule.getDescription())
                .totalProductPrice(totalProductPrice)
                .grandTotal(grandTotal)
                .build();
    }
}
