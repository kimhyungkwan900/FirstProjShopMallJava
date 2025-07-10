package com.example.shop_mall_back.common.config.jwt;

import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.repository.MemberRepository;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final MemberService memberService;
    private final MemberProfileService memberProfileService;

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
        log.debug("토큰 생성 요청: id={}, email={}, role={}", memberId, email, role);
        // 현재시간 생성 및 만료시간 추가
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + duration.toMillis());

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));

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
                .signWith(key, SignatureAlgorithm.HS256)
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
        Claims claims = getClaims(token);

        Long memberId = claims.get("memberId", Long.class);
        String roleName = claims.get("role", String.class);

        // 반드시 DB에서 영속된 Member 객체를 조회
        Member member = memberService.findByIdOrThrow(memberId);
        MemberProfile profile = memberProfileService.findByMemberIdOrThrow(memberId);

        // 도메인 정보를 담은 커스텀 principal 생성
        CustomUserPrincipal principal = new CustomUserPrincipal(member, profile);

        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
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

    // 토큰 생명주기 반환
    public int getAccessTokenExpirySeconds() {
        return (int) ACCESS_TOKEN_DURATION.getSeconds();
    }

    // refresh 토큰 생명주기 반환
    public int getRefreshTokenExpirySeconds() {
        return (int) REFRESH_TOKEN_DURATION.getSeconds();
    }

    public Duration getAccessTokenDuration() {
        return ACCESS_TOKEN_DURATION;
    }

    public Duration getRefreshTokenDuration() {
        return REFRESH_TOKEN_DURATION;
    }
}
