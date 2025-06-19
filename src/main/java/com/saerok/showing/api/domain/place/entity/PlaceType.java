package com.saerok.showing.api.domain.place.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceType {

    HOTEL("HOTEL", "숙소"),
    FESTIVAL("FESTIVAL", "축제"),
    EXPERIENCE("EXPERIENCE", "체험"),
    MARKET("MARKET", "시장"),
    TRAIL("TRAIL", "산책로"),
    MUSEUM("MUSEUM", "박물관"),
    ATTRACTION("ATTRACTION", "관광지");

    private final String key;
    private final String name;
}
