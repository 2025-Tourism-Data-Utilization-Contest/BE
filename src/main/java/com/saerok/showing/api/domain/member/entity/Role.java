package com.saerok.showing.api.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MEMBER("ROLE_MEMBER", "회원", 1),
    ADMIN("ROLE_ADMIN", "관리자", 2);

    private final String key;
    private final String description;
    private final int level;
}
