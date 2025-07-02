package com.example.shop_mall_back.common.service.serviceinterface;

import com.example.shop_mall_back.common.domain.login.Session;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.dto.SessionDTO;

import java.util.Optional;

public interface SessionService {

    void saveSession(Session session);

    Optional<Session> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteAllByMemberId(Long memberId);

    default SessionDTO entityToDTO(Session session){
        return SessionDTO.builder()
                .memberId(session.getMember().getId())
                .accessToken(session.getAccessToken())
                .refreshToken(session.getRefreshToken())
                .ipAddress(session.getIpAddress())
                .expiresAt(session.getExpiresAt())
                .build();
    }

    default Session dtoToEntity(SessionDTO sessionDTO, Member member){
        return Session.builder()
                .member(member)
                .accessToken(sessionDTO.getAccessToken())
                .refreshToken(sessionDTO.getRefreshToken())
                .ipAddress(sessionDTO.getIpAddress())
                .expiresAt(sessionDTO.getExpiresAt())
                .build();
    }
}
