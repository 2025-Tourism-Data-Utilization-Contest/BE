package com.saerok.showing.api.global.filter;

import com.saerok.showing.api.domain.member.service.MemberService;
import com.saerok.showing.api.global.auth.dto.CustomMemberDetails;
import com.saerok.showing.api.global.auth.util.CookieUtil;
import com.saerok.showing.api.global.auth.util.JwtUtil;
import com.saerok.showing.api.global.auth.util.TokenValidator;
import com.saerok.showing.api.global.utils.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final TokenValidator tokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String accessToken = extractAccessToken(request);
        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        tokenValidator.validateAccessToken(accessToken);

        String email = jwtUtil.getEmail(accessToken);
        CustomMemberDetails memberDetails = new CustomMemberDetails(memberService.findByEmail(email));
        setAuthentication(memberDetails);
        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        return CookieUtil.getValue(request, SecurityConstants.ACCESS_TOKEN_COOKIE_NAME)
            .orElseGet(() -> extractFromHeader(request));
    }

    private String extractFromHeader(HttpServletRequest request) {
        String bearer = request.getHeader(SecurityConstants.AUTH_HEADER);
        return (StringUtils.hasText(bearer) && bearer.startsWith(SecurityConstants.BEARER_PREFIX))
            ? bearer.substring(SecurityConstants.BEARER_PREFIX.length())
            : null;
    }

    private void setAuthentication(CustomMemberDetails memberDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
