package com.saerok.showing.api.domain.theme.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DayTime {

    DAY("DAY", "낮"),
    NIGHT("NIGHT", "밤");

    private final String key;
    private final String name;
}
