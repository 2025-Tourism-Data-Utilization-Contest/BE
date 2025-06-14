package com.saerok.showing.api.global.auth.domain;

import com.saerok.showing.api.global.auth.dto.OAuth2MemberDto;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {

    private final OAuth2MemberDto oAuth2MemberDto;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2MemberDto.authorities();
    }

    @Override
    public String getName() {
        return oAuth2MemberDto.name();
    }

    public String getEmail() {
        return oAuth2MemberDto.email();
    }
}
