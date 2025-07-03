package com.example.shop_mall_back.common.config.oauth2;

import com.example.shop_mall_back.common.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    // Cookie 저장시 사용할 이름
    private static final String SHOP_MALL_COOKIE = "SHOP_MALL_OAUTH2_AUTH_REQUEST";

    // 쿠키를 읽어 역 직렬화
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, SHOP_MALL_COOKIE)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class)).orElse(null);
    }

    // 요청 정보 쿠키에 저장
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null){
            CookieUtils.deleteCookie(request, response, SHOP_MALL_COOKIE);
            return;
        }
        CookieUtils.addCookie(response,SHOP_MALL_COOKIE,CookieUtils.serialize(authorizationRequest),180);
    }

    // 인증 정보를 쿠키에서 제거 후 반환
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        CookieUtils.deleteCookie(request, response, SHOP_MALL_COOKIE);
        return authorizationRequest;
    }
}
