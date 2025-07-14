package com.example.shop_mall_back.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {


        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)     // JavaScript 에서 접근불가 설정
                .secure(true)       // HTTPS 환경에서만 쿠키 전송 HTTP 로 전송하지 않아 MITM 방지 가능
                .sameSite("Lax")    // 일부 안전한 크로스 도메인 요청(GET, top-level navigation)만 허용
                .path("/")          // 쿠키 유효 경로
                .maxAge(Duration.ofSeconds(maxAge))
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        ResponseCookie expiredCookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
    }

    public static String serialize(Object obj) {
        try {
            return Base64.getUrlEncoder()
                    .encodeToString(SerializationUtils.serialize(obj));
        } catch (Exception e) {
            throw new IllegalArgumentException("직렬화 실패", e);
        }
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        try {
            return clazz.cast(SerializationUtils.deserialize(
                    Base64.getUrlDecoder().decode(cookie.getValue())));
        } catch (Exception e) {
            throw new IllegalArgumentException("역직렬화 실패", e);
        }
    }
}
