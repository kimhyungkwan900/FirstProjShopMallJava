package com.example.shop_mall_back.common.repository;

import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

    @Modifying
    // @Query("UPDATE MemberAddress SET isDefault = false WHERE member.id = :memberId AND isDefault = true")         이렇게 작성하였으나 GPT 검수과정에서 ma 로 명시해주는것이 좋다하여 변경
    @Query("UPDATE MemberAddress ma SET ma.isDefault = false WHERE ma.member.id = :memberId AND ma.isDefault = true")
    void resetDefaultAddressByMemberId(@Param("memberId") Long memberId);

    // 기본 제공메서드 memberId 개수 반환
    long countByMemberId(Long memberId);

    // 회원 ID로 해당 사용자의 모든 배송지를 조회하는 메서드
    List<MemberAddress> findAllByMemberId(Long memberId);

    /**
     * 특정 회원의 특정 배송지 ID를 기준으로 배송지 1건을 조회하는 메서드
     * - 주로 배송지 삭제나 수정 시 본인 확인용으로 사용
     */
    MemberAddress findByIdAndMemberId(Long addressId, Long memberId);

    /**
     * 특정 회원의 기본 배송지(기본 설정된 배송지)를 조회하는 메서드
     * - isDefault = true 인 배송지 1건을 반환
     */
    MemberAddress findByMemberIdAndIsDefaultTrue(Long memberId);

    List<MemberAddress> findByMember(Member member);
}
