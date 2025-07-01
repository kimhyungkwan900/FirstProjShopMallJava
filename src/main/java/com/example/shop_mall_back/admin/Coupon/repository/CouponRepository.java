package com.example.shop_mall_back.admin.Coupon.repository;


import com.example.shop_mall_back.admin.Coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    //쿠폰 중복 여부 확인
    boolean existsByCouponCode(String couponCode);
}
