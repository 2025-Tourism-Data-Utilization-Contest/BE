package com.saerok.showing.api.domain.review.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ReviewTargetType {

    THEME("THEME", "테마"),
    PLACE("PLACE", "장소");

    private final String key;
    private final String name;
}
