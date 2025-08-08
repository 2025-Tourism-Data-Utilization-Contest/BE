package com.saerok.showing.api.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostType {

    NORMAL("NORMAL", "일반"),
    POLL("POLL", "투표");

    private final String key;
    private final String name;
}
