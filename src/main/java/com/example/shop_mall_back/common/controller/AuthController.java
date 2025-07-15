package com.example.shop_mall_back.common.controller;

import com.example.shop_mall_back.common.Exception.InactiveAccountException;
import com.example.shop_mall_back.common.Exception.InvalidPasswordException;
import com.example.shop_mall_back.common.Exception.MemberNotFoundException;
import com.example.shop_mall_back.common.config.CustomUserPrincipal;
import com.example.shop_mall_back.common.config.jwt.TokenProvider;
import com.example.shop_mall_back.common.constant.LoginResult;
import com.example.shop_mall_back.common.constant.LoginType;
import com.example.shop_mall_back.common.constant.Role;
import com.example.shop_mall_back.common.domain.member.Member;
import com.example.shop_mall_back.common.domain.login.Session;
import com.example.shop_mall_back.common.domain.member.MemberProfile;
import com.example.shop_mall_back.common.dto.LoginRequestDTO;
import com.example.shop_mall_back.common.dto.MemberWithProfileDTO;
import com.example.shop_mall_back.common.repository.SessionRepository;
import com.example.shop_mall_back.common.service.serviceinterface.LoginHistoryService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberProfileService;
import com.example.shop_mall_back.common.service.serviceinterface.MemberService;
import com.example.shop_mall_back.common.utils.CookieConstants;
import com.example.shop_mall_back.common.utils.CookieUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final SessionRepository sessionRepository;
    private final LoginHistoryService loginHistoryService;
    private final MemberProfileService memberProfileService;

    // 현재 로그인 중인 사용자 정보 반환
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(@AuthenticationPrincipal CustomUserPrincipal principal){
        //이메일로 사용자 조회
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // Member 객체 직접 접근 가능
        Member member = principal.getMember();
        MemberProfile profile = principal.getProfile();

        MemberWithProfileDTO dto = new MemberWithProfileDTO(
                member.getId(),
                member.getEmail(),
                member.getUserId(),
                member.getPhoneNumber(),
                profile.getNickname(),
                profile.getRole(),
                profile.getGrade(),
                profile.getGender(),
                profile.getAge(),
                profile.getProfileImgUrl()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request){
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

            CustomUserPrincipal principal = new CustomUserPrincipal(member, memberProfileService.findByMemberIdOrThrow(member.getId()));
            Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 쿠키 저장
            saveTokenCookies(response, accessToken, refreshToken);

            return ResponseEntity.ok(Map.of(
                    "userId", member.getId(),
                    "role", role.name()
            ));
        }
        catch (IllegalArgumentException e) { // 실패

            Member member = memberService.findByUserId(loginRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

            loginHistoryService.recordLogin(
                    member,
                    getClientIp(request),
                    request.getHeader("User-Agent"),
                    LoginResult.FAIL,
                    LoginType.NORMAL
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ID 또는 비밀번호를 확인해주세요");
        }
        catch (InactiveAccountException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("비활성화된 계정입니다.");
        }
        catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
        }
        catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 사용자입니다.");
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
        String refreshToken = CookieUtils.getCookie(request, CookieConstants.REFRESH_TOKEN)
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
        CookieUtils.deleteCookie(request, response, CookieConstants.ACCESS_TOKEN);
        CookieUtils.deleteCookie(request, response, CookieConstants.REFRESH_TOKEN);

        return ResponseEntity.ok(Map.of("message", "로그아웃 완료"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response){
        // 요청들어온 refresh_token 쿠키 조회
        String refreshToken = CookieUtils.getCookie(request, CookieConstants.REFRESH_TOKEN)
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
        CookieUtils.addCookie(response, CookieConstants.ACCESS_TOKEN, newAccessToken, tokenProvider.getAccessTokenExpirySeconds());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount(@AuthenticationPrincipal CustomUserPrincipal principal,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Member member = principal.getMember();

        if (!member.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 탈퇴된 계정입니다.");
        }

        // 회원 비활성화
        memberService.deActivateMember(member.getId());

        // 로그인 기록 logoutTime 갱신
        loginHistoryService.recordLogout(member);

        // 세션 제거
        String refreshToken = CookieUtils.getCookie(request, CookieConstants.REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken != null) {
            sessionRepository.findByRefreshToken(refreshToken).ifPresent(sessionRepository::delete);
        }

        // 쿠키 삭제
        CookieUtils.deleteCookie(request, response, CookieConstants.ACCESS_TOKEN);
        CookieUtils.deleteCookie(request, response, CookieConstants.REFRESH_TOKEN);

        return ResponseEntity.ok(Map.of("message", "회원 탈퇴(비활성화) 완료"));
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
        CookieUtils.addCookie(response, CookieConstants.ACCESS_TOKEN, accessToken, tokenProvider.getAccessTokenExpirySeconds());
        CookieUtils.addCookie(response, CookieConstants.REFRESH_TOKEN, refreshToken, tokenProvider.getRefreshTokenExpirySeconds());
    }
    // </editor-fold>
}
