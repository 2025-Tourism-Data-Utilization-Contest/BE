package com.saerok.showing.api.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {

    NORMAL("NORMAL", "일반"),
    POLL("POLL", "투표"),
    ROUTE("ROUTE", "여행계획");

    private final String key;
    private final String name;
}
