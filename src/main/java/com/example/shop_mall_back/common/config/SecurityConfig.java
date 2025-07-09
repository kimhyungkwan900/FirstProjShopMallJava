package com.example.shop_mall_back.common.config;

import com.example.shop_mall_back.common.config.jwt.TokenAuthenticationFilter;
import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.config.oauth2.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.shop_mall_back.common.config.oauth2.OAuth2SuccessHandler;
import com.example.shop_mall_back.common.repository.SessionRepository;
import com.example.shop_mall_back.common.service.oauthService.GoogleOAuthService;
import com.example.shop_mall_back.common.service.oauthService.KakaoOAuthService;
import com.example.shop_mall_back.common.service.oauthService.NaverOAuthService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
@Log4j2
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository authRequestRepo() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(
            TokenProvider tokenProvider,
            SessionRepository sessionRepository,
            MemberService memberService,
            MemberProfileService memberProfileService,
            OAuth2AuthorizationRequestBasedOnCookieRepository authRequestRepo
    ) {
        return new OAuth2SuccessHandler(tokenProvider, sessionRepository, memberService, authRequestRepo, memberProfileService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService,
                                           TokenAuthenticationFilter tokenAuthenticationFilter,OAuth2SuccessHandler oAuth2SuccessHandler) throws Exception {
        http
                // CSRF 보호 비활성화 TODO: 개발 후 활성화
                .csrf(AbstractHttpConfigurer::disable)
                //
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 기본 로그인 FORM 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 경로 요청에 따른 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 비인증 접근 가능
                        .requestMatchers("/oauth2/**", "/login/**", "/api/auth/**", "/css/**", "/js/**", "/images/**","/api/**","/api/members/signup").permitAll()
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // ✅ 쿠키 허용
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
