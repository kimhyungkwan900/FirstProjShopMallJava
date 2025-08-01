package com.example.shop_mall_back.common.config;

import com.example.shop_mall_back.common.config.jwt.TokenAuthenticationFilter;
import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.config.oauth2.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.example.shop_mall_back.common.config.oauth2.OAuth2FailureHandler;
import com.example.shop_mall_back.common.config.oauth2.OAuth2SuccessHandler;
import com.example.shop_mall_back.common.repository.SessionRepository;
import com.example.shop_mall_back.common.service.oauthService.GoogleOAuthService;
import com.example.shop_mall_back.common.service.oauthService.KakaoOAuthService;
import com.example.shop_mall_back.common.service.oauthService.NaverOAuthService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//후에 제거
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(
            TokenProvider tokenProvider,
            SessionRepository sessionRepository,
            MemberService memberService,
            MemberProfileService memberProfileService,
            AuthorizationRequestRepository<OAuth2AuthorizationRequest> authRequestRepo
    ) {
        return new OAuth2SuccessHandler(tokenProvider, sessionRepository, memberService, authRequestRepo, memberProfileService);
    }

    @Bean
    public OAuth2FailureHandler oAuth2FailureHandler() {
        return new OAuth2FailureHandler("http://localhost:5173");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService,
                                           TokenAuthenticationFilter tokenAuthenticationFilter, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailureHandler oAuth2FailureHandler) throws Exception {
        http
                .csrf(csrf ->csrf.csrfTokenRepository(cookieCsrfTokenRepository())
                        .ignoringRequestMatchers("/api/login","/api/members/signup","/api/csrf-token")) //csrf 설정
                //
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 예외 처리 설정: API 는 JSON 응답, 그 외는 기본 동작
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor( // REST API 용: 401 응답
                                (req, res, authException) -> {
                                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    res.setContentType("application/json;charset=UTF-8");
                                    res.getWriter().write("{\"error\": \"로그인이 필요합니다.\"}");
                                },
                                new AntPathRequestMatcher("/api/**")
                        )
                )
                // 기본 로그인 FORM 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 경로 요청에 따른 인가 설정
                .authorizeHttpRequests(auth -> auth
                        // 비인증 접근 가능
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/**", "/api/members/signup", "/css/**", "/js/**", "/images/**","api/products/**","api/banner/**").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
                        // 그외 인증 접근
                        .anyRequest().authenticated()
                )
                // OAuth2Login 설정
                .oauth2Login(oauth2 -> oauth2
                        //
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestRepository(authRequestRepo())
                        )
                        // 로그인 성공시 handler
                        .successHandler(oAuth2SuccessHandler)
                        // 로그인 실패시 handler
                        .failureHandler(oAuth2FailureHandler)
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
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));                          // 정확한 origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));   // 명시적 메서드
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept","X-CSRF-TOKEN"));
        configuration.setAllowCredentials(true);                                                    // 쿠키 허용

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

    private CookieCsrfTokenRepository cookieCsrfTokenRepository() {
        CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
        // 기본 헤더명은 "X-XSRF-TOKEN" 인데, axios에서 "X-CSRF-TOKEN"으로 보냈다면 바꿔줍니다:
        repo.setHeaderName("X-CSRF-TOKEN");
        // 기본 쿠키 이름 "XSRF-TOKEN"도 그대로 쓰거나 필요에 따라 변경 가능
        return repo;
    }
}
