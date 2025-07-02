package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.domain.login.Session;

import java.util.Optional;

public interface SessionService {

    void saveSession(Session session);

    Optional<Session> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    public void deleteAllByMemberId(Long memberId);
}
