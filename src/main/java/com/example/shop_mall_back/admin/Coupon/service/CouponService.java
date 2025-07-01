package com.example.shop_mall_back.admin.Coupon.service;

import com.example.shop_mall_back.admin.Coupon.domain.Coupon;
import com.example.shop_mall_back.admin.Coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;

    //쿠폰 등록
    //1. 중복 코드 체크
    public Long createCoupon(Coupon coupon){
        if(couponRepository.existsByCouponCode(coupon.getCouponCode())){
            throw new IllegalArgumentException("이미 존재하는 쿠폰 입니다");
        }
        return null;
}
