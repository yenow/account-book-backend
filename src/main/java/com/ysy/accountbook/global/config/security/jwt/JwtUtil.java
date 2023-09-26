package com.ysy.accountbook.global.config.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    public String generateAccessToken(String refreshToken) {
        return "";
    }

    public String resolveToken(HttpServletRequest request) {
        return "";
    }

    public Authentication getAuthentication(String accessToken) {
        return null;
    }
}