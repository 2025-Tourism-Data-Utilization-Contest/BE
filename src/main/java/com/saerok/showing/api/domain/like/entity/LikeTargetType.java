package com.saerok.showing.api.domain.like.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeTargetType {

    POST("POST", "게시글"),
    COMMENT("COMMENT", "댓글"),
    ROUTE("ROUTE", "여행 경로"),
    POLL("POLL", "투표"),
    POLL_OPTION("POLL_OPTION", "투표 후보 여행 경로");

    private final String key;
    private final String name;
}
