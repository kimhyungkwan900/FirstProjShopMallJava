package com.example.shop_mall_back.user.Cart.repository;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.user.Cart.domain.RestockAlarm;
import com.example.shop_mall_back.user.Cart.dto.RestockAlarmDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 재입고 알림 관련 데이터베이스 접근을 위한 Repository 인터페이스
 * - JpaRepository를 확장하여 CRUD 및 커스텀 조회 기능 제공
 */
public interface RestockAlarmRepository extends JpaRepository<RestockAlarm, Long> {

    /**
     * 특정 회원이 특정 상품에 대해 이미 재입고 알림을 신청했는지 여부 확인
     */
    boolean existsByMember_IdAndProduct_Id(Long memberId, Long productId);


    void deleteByMember_IdAndProduct_Id(Long memberId, Long productId);

    List<RestockAlarm> findByMember_Id(Long memberId);
}

