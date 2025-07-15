package com.example.shop_mall_back.common.config.oauth2;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    private final String frontRedirectBaseUrl;

    public OAuth2FailureHandler(String frontRedirectBaseUrl) {
        this.frontRedirectBaseUrl = frontRedirectBaseUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontRedirectBaseUrl + "/oauth2/error")
                .queryParam("error", "oauth_failed")
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}