package com.example.shop_mall_back.common.config;

import com.example.shop_mall_back.common.config.jwt.TokenAuthenticationFilter;
import com.example.shop_mall_back.common.config.oauth2.OAuth2SuccessHandler;
import com.example.shop_mall_back.common.service.oauthService.GoogleOAuthService;
import com.example.shop_mall_back.common.service.oauthService.KakaoOAuthService;
import com.example.shop_mall_back.common.service.oauthService.NaverOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService,
                                           TokenAuthenticationFilter tokenAuthenticationFilter) throws Exception {

        http
                // CSRF 보호 비활성화 TODO: 개발 후 활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 경로 요청에 따른 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 비인증 접근 가능
                        .requestMatchers("/oauth2/**", "/login/**", "/api/auth/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // 그외 전부 인증후 접근
                        .anyRequest().authenticated()
                )
                // OAuth2Login 설정
                .oauth2Login(oauth2 -> oauth2
                        // 로그인 성공시 handler
                        .successHandler(oAuth2SuccessHandler)
                        // 사용자 정보 가져오기
                        .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService))
                )
                // JWT Token 을 위한 커스텀 Filter
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(
            KakaoOAuthService kakaoOAuthService,
            GoogleOAuthService googleOAuthService,
            NaverOAuthService naverOAuthService
    ) {
        return userRequest -> {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            // 제공자 타입에 따른 서비스 연결
            return switch (registrationId) {
                case "kakao" -> kakaoOAuthService.loadUser(userRequest);
                case "naver" -> naverOAuthService.loadUser(userRequest);
                case "google" -> googleOAuthService.loadUser(userRequest);
                default -> throw new OAuth2AuthenticationException("지원하지 않는 타입의 소셜 로그인 입니다.");
            };
        };
    }
}
