package com.example.shop_mall_back.common.config.jwt;

import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import com.example.shop_mall_back.common.utils.CookieConstants;
import com.example.shop_mall_back.common.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.shop_mall_back.common.domain.member.QMember.member;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 회원가입 요청은 무조건 통과
        if (requestURI.equals("/api/members/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더 확인
        String accessToken = CookieUtils.getCookie(request, CookieConstants.ACCESS_TOKEN)
                .map(Cookie::getValue)
                .orElse(null);
        
        // 토큰 존재 & 유효성 검사
        if (accessToken != null && tokenProvider.validateToken(accessToken)) {
            // 이미 인증된 상태가 아니라면
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // 토큰으로부터 인증 객체 추출
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                // SecurityContext 에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
