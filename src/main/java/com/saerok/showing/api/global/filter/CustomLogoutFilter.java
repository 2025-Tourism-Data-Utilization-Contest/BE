package com.saerok.showing.api.global.filter;

import com.saerok.showing.api.domain.member.service.MemberService;
import com.saerok.showing.api.global.auth.service.ReissueService;
import com.saerok.showing.api.global.auth.util.CookieUtil;
import com.saerok.showing.api.global.auth.util.JwtUtil;
import com.saerok.showing.api.global.auth.util.TokenValidator;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.properties.JwtProperties;
import com.saerok.showing.api.global.utils.ResponseUtil;
import com.saerok.showing.api.global.utils.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final MemberService memberService;
    private final ReissueService reissueService;
    private final TokenValidator tokenValidator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (!isLogoutRequest(httpRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractRefreshToken(httpRequest);
        tokenValidator.validateRefreshToken(refreshToken);

        String email = jwtUtil.getEmail(refreshToken);
        Long memberId = memberService.findByEmail(email).getId();

        reissueService.deleteById(memberId);
        CookieUtil.deleteCookie(httpResponse, SecurityConstants.REFRESH_TOKEN_COOKIE_NAME,
            jwtProperties.getCookieDomain());
        ResponseUtil.writeSuccess(httpResponse);
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return SecurityConstants.LOGOUT_ENDPOINT.equals(request.getRequestURI())
            && SecurityConstants.POST_METHOD.equalsIgnoreCase(request.getMethod());
    }

    private String extractRefreshToken(HttpServletRequest request) {
        return CookieUtil.getValue(request, SecurityConstants.REFRESH_TOKEN_COOKIE_NAME)
            .orElseThrow(() -> ShowingException.from(ErrorCode.NOT_FOUND_REFRESH_TOKEN));
    }
}
