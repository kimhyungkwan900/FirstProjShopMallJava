package com.example.shop_mall_back.admin.Coupon.service;

import com.example.shop_mall_back.admin.Coupon.domain.Coupon;
import com.example.shop_mall_back.admin.Coupon.dto.CouponDto;
import com.example.shop_mall_back.admin.Coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;

    //쿠폰 등록
    //1. 중복 코드 체크
    public Long createCoupon(CouponDto dto) {
        if (couponRepository.existsByCouponCode(dto.getCouponCode())) {
            throw new IllegalArgumentException("이미 존재하는 쿠폰 입니다");
        }
      
        //2. dto -> entity 변환
        Coupon coupon = dto.toEntity();

        //3. db에 저장
        couponRepository.save(coupon);

        return coupon.getId(); //쿠폰 고유번호 반환
    }

    //전체 목록 조회
    @Transactional(readOnly = true)
    public List<CouponDto> getAllCoupons(){

        //db에서 쿠폰 가져오기
        List<Coupon> coupons = couponRepository.findAll();

        //가지고 온 쿠폰들을 dto로 바꿔서 리스트 반환
        return coupons.stream()
                .map(CouponDto::new) //각각의 쿠폰을 CouponDto로 변환
                .collect(Collectors.toList());
    }

    //하나만 조회
    @Transactional(readOnly = true)
    public CouponDto getCoupon(Long id){

        //id에 해당하는 쿠폰을 db에서 찾기
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 쿠폰이 없습니다"));

        //찾은 쿠폰을 dto로 바꿔서 반환
        return new CouponDto(coupon);
    }

    //쿠폰 수정하기
    public void updateCoupon(Long id, CouponDto dto){

        //수정할 쿠폰 찾기
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("수정할 쿠폰이 없습니다"));

        coupon.setCouponCode(dto.getCouponCode());
        coupon.setDiscountAmount(dto.getDiscountAmount());
        coupon.setMinimumOrderAmount(dto.getMinimumOrderAmount());
        coupon.setStartDate(coupon.getStartDate());
        coupon.setEndDate(coupon.getEndDate());
        coupon.setIsActive(dto.getIsActive());
    }

    //쿠폰 삭제하기
    public void deleteCoupon(Long id){
        couponRepository.deleteById(id);
    }
}
