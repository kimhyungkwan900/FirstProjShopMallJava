package com.example.shop_mall_back.user.Cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCouponDto {

    private Long id;            // 사용자 쿠폰 ID
    private Long memberId;      // 사용자 ID
    private Long couponId;      // 쿠폰 ID
    private Boolean isUsed;     // 사용 여부
    private LocalDateTime usedAt; // 사용 시각
}
