package com.saerok.showing.api.global.auth.service;

import com.saerok.showing.api.domain.member.entity.LoginType;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.entity.Role;
import com.saerok.showing.api.domain.member.repository.MemberRepository;
import com.saerok.showing.api.global.auth.domain.RefreshToken;
import com.saerok.showing.api.global.auth.dto.GuestLoginResponse;
import com.saerok.showing.api.global.auth.repository.RefreshTokenRepository;
import com.saerok.showing.api.global.auth.util.CookieUtil;
import com.saerok.showing.api.global.auth.util.TokenProvider;
import com.saerok.showing.api.global.properties.JwtProperties;
import com.saerok.showing.api.global.utils.SecurityConstants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestAuthService {

    private static final String PREFIX = "guest";
    private static final String DOMAIN = "@guest.com";

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public GuestLoginResponse createGuestLogin(HttpServletResponse response) {

        Member guestMember = createGuest();

        String accessToken = tokenProvider.generateAccessToken(guestMember);
        String refreshToken = tokenProvider.generateRefreshToken(guestMember);
        refreshTokenRepository.save(new RefreshToken(guestMember.getId(), refreshToken));

        CookieUtil.addCookie(response, SecurityConstants.ACCESS_TOKEN_COOKIE_NAME, accessToken,
            jwtProperties.getAccessTokenExpiration(), jwtProperties.getCookieDomain());
        CookieUtil.addCookie(response, SecurityConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken,
            jwtProperties.getRefreshTokenExpiration(), jwtProperties.getCookieDomain());

        return GuestLoginResponse.toDto(guestMember);
    }

    private Member createGuest() {
        int nextNumber = generateNextNumber();
        String email = PREFIX + nextNumber + DOMAIN;
        String displayName = String.format("게스트%d", nextNumber);
        return memberRepository.save(buildGuest(email, displayName));
    }

    // 다음 게스트 번호 생성
    private int generateNextNumber() {
        return memberRepository.findTopByEmailStartingWithOrderByIdDesc(PREFIX)
            .map(m -> extractNumber(m.getEmail()) + 1)
            .orElse(1);
    }

    // 번호 추출 (guest12@guest.com → 12)
    private int extractNumber(String email) {
        try {
            String local = email.substring(0, email.indexOf('@'));
            return Integer.parseInt(local.replace(PREFIX, ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private Member buildGuest(String email, String displayName) {
        return Member.builder()
            .email(email)
            .name(displayName)
            .nickname(displayName)
            .role(Role.MEMBER)
            .loginType(LoginType.GUEST)
            .build();
    }
}
