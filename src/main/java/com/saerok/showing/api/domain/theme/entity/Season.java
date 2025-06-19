package com.saerok.showing.api.domain.theme.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Season {

    SPRING("SPRING", "봄"),
    SUMMER("SUMMER", "여름"),
    AUTUMN("AUTUMN", "가을"),
    WINTER("WINTER", "겨울");

    private final String key;
    private final String name;
}
