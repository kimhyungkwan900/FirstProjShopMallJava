package com.example.shop_mall_back.admin.Coupon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CouponDto {

    private Long id; //쿠폰ID
    private String couponCode; //쿠폰 코드
    private Integer discountAmount; //할인 금액
    private Integer minimumOrderAmount; //최소주문금액
    private LocalDate createdAt; //쿠폰 시작일
    private LocalDate endDate;//쿠폰 마감일
    private Boolean isActive; //사용 가능 여부
}
