package com.example.shop_mall_back.common.repository;

import com.example.shop_mall_back.common.domain.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    MemberProfile findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
