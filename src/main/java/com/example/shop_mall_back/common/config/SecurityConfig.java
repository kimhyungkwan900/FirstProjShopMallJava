package com.example.shop_mall_back.common.config;

import com.example.shop_mall_back.common.service.oauthService.KakaoOAuthService;
import com.querydsl.core.annotations.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean   //TODO: defaultSuccessUrl 메인페이지로 변경할 것
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())               //csrf 비활성화 TODO: 개발 종료후 삭제
                .authorizeHttpRequests(auth -> auth    //URL 접근권한 설정 아래 requestMatchers 는 인증없이 접근 가능
                        .requestMatchers("/oauth2/**","login/**","/css/**","/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()                               //그 외 모든 요청 인증필요
                )
                .oauth2Login(oauth2 -> oauth2       // OAuth2 Login
                        .defaultSuccessUrl("/main",true)    // 로그인 이후 Redirection 경로
                        .userInfoEndpoint(userInfo ->          // 사용자정보 -> oauth2UserService 에서 처리
                                userInfo.userService(oauth2UserService())
                        )
                );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new KakaoOAuthService();
    }
}
