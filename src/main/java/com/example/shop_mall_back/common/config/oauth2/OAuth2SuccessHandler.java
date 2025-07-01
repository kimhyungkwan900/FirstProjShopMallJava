package com.example.shop_mall_back.common.config.oauth2;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.RefreshToken;
import com.example.shop_mall_back.common.repository.RefreshTokenRepository;
import com.example.shop_mall_back.common.service.MemberService;
import com.example.shop_mall_back.common.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authRequestRepo;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 유저 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = oAuth2User.getId();
        String email = oAuth2User.getEmail();
        Role role = oAuth2User.getRole();

        // 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(memberId, email, role);
        String refreshToken = tokenProvider.generateRefreshToken();

        // RefreshToken 저장
        RefreshToken tokenEntity = RefreshToken.builder()
                .memberId(memberId)
                .token(accessToken)
                .build();

        refreshTokenRepository.save(tokenEntity);

        // 기존 쿠키 제거
        authRequestRepo.removeAuthorizationRequest(request, response);

        // 쿠키에 토큰 저장
        CookieUtils.addCookie(response,"access_token",accessToken,tokenProvider.getAccessTokenExpirySeconds());
        CookieUtils.addCookie(response,"refresh_token",refreshToken,tokenProvider.getRefreshTokenExpirySeconds());

        // 리다이렉트
        response.sendRedirect("http://localhost:5173/");
    }
}
