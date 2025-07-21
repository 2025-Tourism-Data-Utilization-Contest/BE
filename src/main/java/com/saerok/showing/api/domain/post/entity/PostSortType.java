package com.saerok.showing.api.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostSortType {

    LATEST("LATEST", "최신순"),
    POPULAR("POPULAR", "인기순");

    private final String key;
    private final String name;
}
