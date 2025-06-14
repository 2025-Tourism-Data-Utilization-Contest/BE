package com.saerok.showing.api.global.auth.service;

import com.saerok.showing.api.domain.member.entity.Role;
import com.saerok.showing.api.global.auth.domain.RefreshToken;
import com.saerok.showing.api.global.auth.repository.RefreshTokenRepository;
import com.saerok.showing.api.global.auth.util.CookieUtil;
import com.saerok.showing.api.global.auth.util.TokenProvider;
import com.saerok.showing.api.global.auth.util.TokenValidator;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.properties.JwtProperties;
import com.saerok.showing.api.global.utils.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final TokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final TokenValidator tokenValidator;
    private final RefreshTokenRepository refreshTokenRepository;

    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        tokenValidator.validateRefreshToken(refreshToken);

        String email = tokenValidator.extractEmail(refreshToken);
        Role role = tokenValidator.extractRole(refreshToken);
        Long memberId = tokenValidator.extractMemberId(refreshToken);

        refreshTokenRepository.deleteById(String.valueOf(memberId));

        String newAccessToken = tokenProvider.generateAccessToken(memberId, email, role);
        String newRefreshToken = tokenProvider.generateRefreshToken(memberId, email, role);

        refreshTokenRepository.save(new RefreshToken(memberId, newRefreshToken));

        CookieUtil.addCookie(response, SecurityConstants.ACCESS_TOKEN_COOKIE_NAME, newAccessToken,
            jwtProperties.getAccessTokenExpiration(), jwtProperties.getCookieDomain());
        CookieUtil.addCookie(response, SecurityConstants.REFRESH_TOKEN_COOKIE_NAME, newRefreshToken,
            jwtProperties.getRefreshTokenExpiration(), jwtProperties.getCookieDomain());
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        return CookieUtil.getValue(request, SecurityConstants.REFRESH_TOKEN_COOKIE_NAME)
            .orElseThrow(() -> ShowingException.from(ErrorCode.TOKEN_NOT_FOUND));
    }


    public void deleteById(Long memberId) {
        refreshTokenRepository.deleteById(String.valueOf(memberId));
    }
}
