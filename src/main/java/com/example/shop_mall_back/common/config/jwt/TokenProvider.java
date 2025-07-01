package com.example.shop_mall_back.common.config.jwt;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private static final Duration ACCESS_TOKEN_DURATION =Duration.ofMinutes(30);
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);

    // 사용자 ID 와 email 을 받아 AccessToken 생성
    public String generateAccessToken(Long memberId, String email, Role role) {
        return generateToken(memberId, email, role, ACCESS_TOKEN_DURATION);
    }

    // 사용자 정보가없는 RefreshToken 생성
    public String generateRefreshToken(){
        return generateToken(null, null, null, REFRESH_TOKEN_DURATION);
    }

    
    private String generateToken(Long memberId, String email, Role role, Duration duration) {
        // 현재시간 생성 및 만료시간 추가
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + duration.toMillis());

        // Header : 토큰의 메타정보
        // 토큰 타입 : JWT_TYPE
        // 토큰 발급자 : jwtProperties
        // 토큰 발급시각 : now
        // 토큰 만료시각 : Duration
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate);
        
        // 생성된 토큰에 정보 담기
        if (email != null) {
            builder.setSubject(email);
        }
        if (memberId != null) {
            // id 에 memberId 담기
            builder.claim("id", memberId);
        }
        if (role != null) {
            // role 에 role.name() 담기
            builder.claim("role", role.name());
        }

        // 상단 정보기반 builder
        return builder
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // token 의 claims 에서 role 정보를 가져와 인증 객체 반환
    public Authentication getAuthentication(String token) {
        // claims 가져오기
        Claims claims = getClaims(token);
        
        // role 에 들어간 정보 가져오기
        String role = claims.get("role", String.class);

        // Security 권한 객체로 변환 Ex) ROLE_USER
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        // 객체 반환 ( 사용자 정보 / JWT 토큰 / 권한 목록 등이 들어있음)
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User
                (claims.getSubject(),"",authorities),token,authorities);
    }

    // claim 에서 id 반환받기
    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    // claim 반환받기
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
