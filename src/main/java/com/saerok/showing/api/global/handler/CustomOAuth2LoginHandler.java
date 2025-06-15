package com.saerok.showing.api.global.handler;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.service.MemberService;
import com.saerok.showing.api.global.auth.domain.CustomOAuth2Member;
import com.saerok.showing.api.global.auth.domain.RefreshToken;
import com.saerok.showing.api.global.auth.repository.RefreshTokenRepository;
import com.saerok.showing.api.global.auth.util.CookieUtil;
import com.saerok.showing.api.global.auth.util.TokenProvider;
import com.saerok.showing.api.global.properties.FrontendProperties;
import com.saerok.showing.api.global.properties.JwtProperties;
import com.saerok.showing.api.global.utils.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final JwtProperties jwtProperties;
    private final MemberService memberService;
    private final FrontendProperties frontendProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        CustomOAuth2Member customUserDetails = (CustomOAuth2Member) authentication.getPrincipal();
        String email = customUserDetails.getEmail();
        Member member = memberService.findByEmail(email);

        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);
        refreshTokenRepository.save(new RefreshToken(member.getId(), refreshToken));

        CookieUtil.addCookie(response, SecurityConstants.ACCESS_TOKEN_COOKIE_NAME, accessToken,
            jwtProperties.getAccessTokenExpiration(), jwtProperties.getCookieDomain());
        CookieUtil.addCookie(response, SecurityConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken,
            jwtProperties.getRefreshTokenExpiration(), jwtProperties.getCookieDomain());

        response.sendRedirect(frontendProperties.getHomeUrl());
    }
}
