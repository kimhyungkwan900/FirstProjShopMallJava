package com.example.shop_mall_back.common.config.jwt;

import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.Member;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private static final Duration ACCESS_TOKEN_DURATION =Duration.ofMinutes(30);
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofMinutes(30);

    public String generateAccessToken(Long memberId, String email, Role role) {
        return generateToken(memberId, email, role, ACCESS_TOKEN_DURATION);
    }

    public String generateRefreshToken(){
        return generateToken(null, null, null, REFRESH_TOKEN_DURATION);
    }

    private String generateToken(Long memberId, String email, Role role, Duration duration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + duration.toMillis());

//        JwtBuilder builder = Jwts.builder().compact();

        return null;
    }
}
