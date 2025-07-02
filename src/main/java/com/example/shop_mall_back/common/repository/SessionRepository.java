package com.example.shop_mall_back.common.repository;

import com.example.shop_mall_back.common.domain.login.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
    List<Session> findAllByMemberId(Long memberId);
}