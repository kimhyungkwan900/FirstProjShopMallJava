package com.example.shop_mall_back.common.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.Member;
import com.example.shop_mall_back.common.domain.RefreshToken;
import com.example.shop_mall_back.common.dto.LoginRequestDTO;
import com.example.shop_mall_back.common.dto.MemberDTO;
import com.example.shop_mall_back.common.repository.RefreshTokenRepository;
import com.example.shop_mall_back.common.service.MemberService;
import com.example.shop_mall_back.common.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 현재 로그인 중인 사용자 정보 반환
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(@AuthenticationPrincipal(expression = "membername") String email){
        //이메일로 사용자 조회
        MemberDTO memberDto = memberService.getMemberDTOByEmail(email);

        return ResponseEntity.ok(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response){
        // 사용자 인증
        Member member = memberService.authenticate(loginRequestDTO.getUserId(), loginRequestDTO.getPassword());
        
        // 토큰 생성
        String accessToken = tokenProvider.generateAccessToken(member.getId(), member.getEmail(), memberService.getRoleByMember(member));
        String refreshToken = tokenProvider.generateRefreshToken();
        
        // RefeshToken 저장
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken));
        
        // 쿠키에 저장
        CookieUtils.addCookie(response, "access_token", accessToken, tokenProvider.getAccessTokenExpirySeconds());
        CookieUtils.addCookie(response, "refresh_token", refreshToken, tokenProvider.getRefreshTokenExpirySeconds());

        return ResponseEntity.ok().build();
    }

    // logout 시 쿠키에서 토큰 삭제
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        // 쿠키에서 JWT 토큰 삭제
        CookieUtils.deleteCookie(request, response, "access_token");
        CookieUtils.deleteCookie(request, response, "refresh_token");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response){
        // 요청들어온 refresh_token 쿠키 조회
        String refreshToken = CookieUtils.getCookie(request, "refresh_token")
                .map(Cookie::getValue)
                .orElse(null);

        // refreshToken 이 존재하지 않거나 유효성검사 실패시 인증 실패
        if (refreshToken == null || !tokenProvider.validateToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰 입니다.");
        }

        // refreshToken 이 유효할 경우 새로운 토큰 발급
        Long memberId = tokenProvider.getMemberId(refreshToken);
        String email = tokenProvider.getClaims(refreshToken).getSubject();
        Role role = Role.valueOf(tokenProvider.getClaims(refreshToken).get("role",String.class));

        String newAccessToken = tokenProvider.generateAccessToken(memberId, email, role);

        // 새로운 accessToken 을 쿠키에 넣어 응답
        CookieUtils.addCookie(response,"access_token", newAccessToken, tokenProvider.getAccessTokenExpirySeconds());

        return ResponseEntity.ok().build();
    }
}
