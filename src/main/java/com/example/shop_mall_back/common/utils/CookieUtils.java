package com.example.shop_mall_back.common.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return Optional.empty();
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                return Optional.of(cookie);
            }
        }

        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None") // 크로스 도메인 대응
                .path("/")
                .maxAge(Duration.ofSeconds(maxAge))
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,String name){
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return;
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals(name)){
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz){
        return clazz.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }

    public static String serialize(Object object){
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }
}
