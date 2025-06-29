package com.example.shop_mall_back.common.repository;

import com.example.shop_mall_back.common.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

    @Modifying
//    @Query("UPDATE MemberAddress SET isDefault = false WHERE member.id = :memberId AND isDefault = true")         이렇게 작성하였으나 GPT 검수과정에서 ma 로 명시해주는것이 좋다하여 변경
    @Query("UPDATE MemberAddress ma SET ma.isDefault = false WHERE ma.member.id = :memberId AND ma.isDefault = true")
    void resetDefaultAddressByMemberId(@Param("memberId") Long memberId);

//    기본 제공메서드 memberId 개수 반환
    long countByMemberId(Long memberId);
}
