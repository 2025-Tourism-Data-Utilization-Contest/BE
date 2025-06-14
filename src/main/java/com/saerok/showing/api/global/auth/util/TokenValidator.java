package com.saerok.showing.api.global.auth.util;

import com.saerok.showing.api.domain.member.entity.Role;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.utils.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final JwtUtil jwtUtil;

    public void validateRefreshToken(String token) {
        if (!jwtUtil.isValid(token)) {
            throw ShowingException.from(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        if (jwtUtil.isExpired(token)) {
            throw ShowingException.from(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        if (!SecurityConstants.REFRESH_TOKEN_CATEGORY.equals(jwtUtil.getCategory(token))) {
            throw ShowingException.from(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public void validateAccessToken(String token) {
        if (!jwtUtil.isValid(token)) {
            throw ShowingException.from(ErrorCode.INVALID_AUTH_TOKEN);
        }
        if (jwtUtil.isExpired(token)) {
            throw ShowingException.from(ErrorCode.EXPIRED_AUTH_TOKEN);
        }
        if (!SecurityConstants.ACCESS_TOKEN_CATEGORY.equals(jwtUtil.getCategory(token))) {
            throw ShowingException.from(ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    public String extractEmail(String token) {
        return jwtUtil.getEmail(token);
    }

    public Role extractRole(String token) {
        return Role.valueOf(String.valueOf(jwtUtil.getRole(token)));
    }

    public Long extractMemberId(String token) {
        return jwtUtil.getMemberId(token);
    }
}
