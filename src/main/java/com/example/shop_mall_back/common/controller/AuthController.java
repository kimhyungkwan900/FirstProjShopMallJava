package com.example.shop_mall_back.common.controller;

import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.LoginResult;
import com.example.shop_mall_back.common.constant.LoginType;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.login.Session;
import com.example.shop_mall_back.common.dto.LoginRequestDTO;
import com.example.shop_mall_back.common.dto.MemberDTO;
import com.example.shop_mall_back.common.repository.SessionRepository;
import com.example.shop_mall_back.common.service.serviceinterface.LoginHistoryService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import com.example.shop_mall_back.common.utils.CookieUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final SessionRepository sessionRepository;
    private final LoginHistoryService loginHistoryService;

    // 현재 로그인 중인 사용자 정보 반환
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(@AuthenticationPrincipal(expression = "membername") String email){
        //이메일로 사용자 조회
        MemberDTO memberDto = memberService.getMemberDTOByEmail(email);

        return ResponseEntity.ok(memberDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request){

        log.info("로그인 시도");

        //로그인 시도
        try {
            // 사용자 인증
            Member member = memberService.authenticate(loginRequestDTO.getUserId(), loginRequestDTO.getPassword());

            // 권한 확인
            Role role = memberService.getRoleByMember(member);

            // 토큰 생성
            String accessToken = tokenProvider.generateAccessToken(member.getId(), member.getEmail(), role);
            String refreshToken = tokenProvider.generateRefreshToken();

            // 세션 저장
            Session session = Session.builder()
                    .member(member)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .ipAddress(getClientIp(request))
                    .expiresAt(LocalDateTime.now().plusSeconds(tokenProvider.getRefreshTokenExpirySeconds()))
                    .build();
            sessionRepository.save(session);

            // 로그인 기록 저장
            loginHistoryService.recordLogin(
                    member,
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    LoginResult.SUCCESS,
                    LoginType.NORMAL
            );

            // 쿠키 저장
            saveTokenCookies(response, accessToken, refreshToken);

            return ResponseEntity.ok(Map.of("message", "로그인 성공"));
        }
        catch (IllegalArgumentException e) { // 실패

            Member member = memberService.findByUserId(loginRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

            loginHistoryService.recordLogin(
                    member, // 또는 memberService.findByUserId(...)로 식별 가능한 경우
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    LoginResult.FAIL,
                    LoginType.NORMAL
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID 또는 비밀번호를 확인해주세요");
        }
        catch (Exception e) {
            // 예기치 않은 오류 (서버 오류)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }

    }



    // logout 시 쿠키에서 토큰 삭제
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        // 쿠키에서 refresh_token 꺼내기
        String refreshToken = CookieUtils.getCookie(request, "refresh_token")
                .map(Cookie::getValue)
                .orElse(null);

        // 해당 refresh_token 이 DB에 존재하면 삭제
        if (refreshToken != null) {
            sessionRepository.findByRefreshToken(refreshToken).ifPresent(session -> {
                // Member 객체 얻기
                Member member = session.getMember();

                // 세션 삭제
                sessionRepository.delete(session);

                // 최근 로그인 기록의 logoutTime 갱신
                loginHistoryService.recordLogout(member);
            });
        }

        // 쿠키 삭제
        CookieUtils.deleteCookie(request, response, "access_token");
        CookieUtils.deleteCookie(request, response, "refresh_token");

        return ResponseEntity.ok(Map.of("message", "로그아웃 완료"));
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

        Optional<Session> dbToken = sessionRepository.findByRefreshToken(refreshToken);
        if (dbToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록되지 않은 리프레시 토큰입니다.");
        }

        // refreshToken 이 유효할 경우 새로운 토큰 발급
        Claims claims = tokenProvider.getClaims(refreshToken);
        Long memberId = tokenProvider.getMemberId(refreshToken);
        String email = claims.getSubject();
        Role role = Role.valueOf(claims.get("role", String.class));

        String newAccessToken = tokenProvider.generateAccessToken(memberId, email, role);

        // 새로운 accessToken 을 쿠키에 넣어 응답
        CookieUtils.addCookie(response, "access_token", newAccessToken, tokenProvider.getAccessTokenExpirySeconds());

        return ResponseEntity.ok().build();
    }

    // <editor-fold desc="편의성 private 메서드">
    // IP 정보 저장
    private String getClientIp(HttpServletRequest request) {

        // X-Forwarded-For == ip의 값이 들어오는이름
        String ip = request.getHeader("X-Forwarded-For");

        // ip에 값이 있다면 , 기준 첫번째 값 반환
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim(); // 첫 번째 IP만 추출
        }

        // 값이 없어 반환하지 못한다면 기본값 반환 127.0.0.1
        return request.getRemoteAddr();
    }

    private void saveTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        CookieUtils.addCookie(response, "access_token", accessToken, tokenProvider.getAccessTokenExpirySeconds());
        CookieUtils.addCookie(response, "refresh_token", refreshToken, tokenProvider.getRefreshTokenExpirySeconds());
    }
    // </editor-fold>
}
