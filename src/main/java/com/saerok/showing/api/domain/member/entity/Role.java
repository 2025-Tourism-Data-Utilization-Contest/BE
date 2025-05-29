package com.saerok.showing.api.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    DONOR("ROLE_DONOR", "Donor (기부자)"),
    ADMIN("ROLE_ADMIN", "Administrator (관리자)");

    private final String key;
    private final String description;
}
