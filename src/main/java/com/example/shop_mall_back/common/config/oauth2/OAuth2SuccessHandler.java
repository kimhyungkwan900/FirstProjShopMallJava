package com.example.shop_mall_back.common.config.oauth2;

import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.OauthProvider;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.login.Session;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.repository.SessionRepository;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import com.example.shop_mall_back.common.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final SessionRepository sessionRepository;
    private final MemberService memberService;
//    private final OAuth2AuthorizationRequestBasedOnCookieRepository authRequestRepo;
private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo;
    private final MemberProfileService memberProfileService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 유저 정보 가져오기
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Long memberId = oAuth2User.getId();
        String email = oAuth2User.getEmail();
        Role role = oAuth2User.getRole();

        Member member = memberService.findByEmail(email);
        MemberProfile profile = memberProfileService.findByMemberIdOrThrow(memberId);

        // provider와 attributes 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OauthProvider provider = oAuth2User.getProvider();

        // 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(memberId, email, role);
        String refreshToken = tokenProvider.generateRefreshToken();

        // 기본 만료 시간
        long refreshTokenDuration = tokenProvider.getRefreshTokenExpirySeconds();

        // 네이버인 경우 expires_in 문자열 처리
        if (provider == OauthProvider.NAVER) {
            Object rawExpiresIn = attributes.get("expires_in");
            if (rawExpiresIn instanceof String) {
                try {
                    refreshTokenDuration = Long.parseLong((String) rawExpiresIn);
                } catch (NumberFormatException e) {
                    log.warn("네이버 expires_in 형변환 실패, 기본값 사용");
                }
            }
        }

        // RefreshToken 저장
        Session tokenEntity = Session.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(LocalDateTime.now().plus(tokenProvider.getAccessTokenDuration()))
                .ipAddress(request.getRemoteAddr())
                .build();

        sessionRepository.save(tokenEntity);

        // 인증 객체 생성 및 등록
        CustomUserPrincipal principal = new CustomUserPrincipal(member, profile, attributes);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 기존 쿠키 제거
        // CookieUtils.deleteCookie(request, response, "SHOP_MALL_OAUTH2_AUTH_REQUEST");
        authRequestRepo.removeAuthorizationRequest(request, response);

        // 쿠키에 토큰 저장
        CookieUtils.addCookie(response,"access_token",accessToken,tokenProvider.getAccessTokenExpirySeconds());
        CookieUtils.addCookie(response,"refresh_token",refreshToken,tokenProvider.getRefreshTokenExpirySeconds());


        // 리다이렉트
        response.sendRedirect("http://localhost:5173/oauth2/success?accessToken=" + accessToken);
    }
}
