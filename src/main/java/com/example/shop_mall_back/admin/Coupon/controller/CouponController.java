package com.example.shop_mall_back.admin.Coupon.controller;

import com.example.shop_mall_back.admin.Coupon.dto.CouponDto;
import com.example.shop_mall_back.admin.Coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    //쿠폰 등록
    @PostMapping("/create")
    public ResponseEntity<Long> createCoupon(@RequestBody CouponDto dto){
        return null;
    }

    //쿠폰 목록 조회

    //쿠폰 하나만 조회

    //쿠폰 수정

    //쿠폰 삭제


}
