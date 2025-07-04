package com.example.shop_mall_back.user.Cart.service;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.Product;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.user.Cart.domain.RestockAlarm;
import com.example.shop_mall_back.user.Cart.repository.RestockAlarmRepository;
import com.example.shop_mall_back.user.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 재입고 알림 서비스 클래스
 * - 사용자가 품절된 상품에 대해 재입고 알림을 신청할 수 있도록 처리하는 역할을 한다.
 */
@Service
@RequiredArgsConstructor
public class RestockAlarmService {

    private final RestockAlarmRepository restockAlarmRepository; // 재입고 알림 저장소
    private final MemberRepository memberRepository;             // 사용자 정보 저장소
    private final ProductRepository productRepository;           // 상품 정보 저장소

    /**
     * 재입고 알림 신청 처리
     * - 이미 신청한 경우 예외를 던지고, 처음 신청한 경우 DB에 저장
     *
     * @param memberId 알림을 신청하는 회원 ID
     * @param productId 알림을 신청할 상품 ID
     */
    public void requestRestockAlarm(Long memberId, Long productId) {
        // 1. 이미 해당 회원이 이 상품에 대해 알림을 신청했는지 확인
        if (restockAlarmRepository.existsByMember_IdAndProduct_Id(memberId, productId)) {
            throw new IllegalStateException("이미 알림 신청이 되어 있습니다.");
        }

        // 2. 알림이 처음이라면, 회원과 상품을 DB에서 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효한 상품이 아닙니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. 재입고 알림 엔티티 생성 및 정보 설정
        RestockAlarm restockAlarm = new RestockAlarm();
        restockAlarm.setMember(member);     // 신청한 회원 설정
        restockAlarm.setProduct(product);   // 알림 대상 상품 설정

        // 4. 알림 정보 저장
        restockAlarmRepository.save(restockAlarm);
    }
}

