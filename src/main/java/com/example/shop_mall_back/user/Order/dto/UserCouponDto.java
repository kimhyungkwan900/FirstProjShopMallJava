package com.example.shop_mall_back.user.Order.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCouponDto {

    private Long id;
    private Long memberId;
    private Long couponId;
    private Boolean isUsed;
    private LocalDateTime usedAt;
}
