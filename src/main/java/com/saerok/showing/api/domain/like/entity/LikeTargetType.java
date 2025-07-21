package com.saerok.showing.api.domain.like.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeTargetType {

    POST("POST", "게시글"),
    COMMENT("COMMENT", "댓글");

    private final String key;
    private final String name;
}
