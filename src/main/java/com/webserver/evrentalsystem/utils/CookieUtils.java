package com.webserver.evrentalsystem.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public class CookieUtils {

    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";

    public static ResponseCookie createCookie(String name, String value, int maxAge) {
        return ResponseCookie
                .from(name, value)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("None")
                .build();
    }

    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
