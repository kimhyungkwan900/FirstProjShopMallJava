package com.example.shop_mall_back.common.service.serviceimpl;

import com.example.shop_mall_back.common.domain.login.Session;
import com.example.shop_mall_back.common.repository.SessionRepository;
import com.example.shop_mall_back.common.service.serviceinterface.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;

    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    public Optional<Session> findByRefreshToken(String refreshToken) {
        return sessionRepository.findByRefreshToken(refreshToken);
    }

    public void deleteByRefreshToken(String refreshToken) {
        sessionRepository.deleteByRefreshToken(refreshToken);
    }

    public void deleteAllByMemberId(Long memberId) {
        sessionRepository.findAllByMemberId(memberId)
                .forEach(sessionRepository::delete);
    }
}
