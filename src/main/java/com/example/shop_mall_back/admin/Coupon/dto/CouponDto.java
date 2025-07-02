package com.example.shop_mall_back.admin.Coupon.dto;

import com.example.shop_mall_back.admin.Coupon.domain.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CouponDto {

    private Long id; //쿠폰ID
    private String couponCode; //쿠폰 코드
    private Integer discountAmount; //할인 금액
    private Integer minimumOrderAmount; //최소주문금액
    private LocalDate startDate;; //쿠폰 시작일
    private LocalDate endDate;//쿠폰 마감일
    private Boolean isActive; //사용 가능 여부

//    private LocalDateTime createdAt; //생성일
//    private LocalDateTime updatedAt; //수정일

    // 엔티티를 받아서 dto로 바꾸기
    public CouponDto(Coupon coupon) {
        this.id = coupon.getId();
        this.couponCode = coupon.getCouponCode();
        this.discountAmount = coupon.getDiscountAmount();
        this.minimumOrderAmount = coupon.getMinimumOrderAmount();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.isActive = coupon.getIsActive();
//        this.createdAt = coupon.getCreatedAt();
//        this.updatedAt = coupon.getUpdatedAt();
    }

    //DTO -> 엔티티로 바꾸는 메서드
    public Coupon toEntity() {
        Coupon coupon = new Coupon();
        coupon.setId(this.id);
        coupon.setCouponCode(this.couponCode);
        coupon.setDiscountAmount(this.discountAmount);
        coupon.setMinimumOrderAmount(this.minimumOrderAmount);
        coupon.setStartDate(this.startDate);
        coupon.setEndDate(this.endDate);
        coupon.setIsActive(this.isActive);

        return coupon;

    }

}
