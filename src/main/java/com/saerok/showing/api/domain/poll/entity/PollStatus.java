package com.saerok.showing.api.domain.poll.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PollStatus {

    READY("READY", "대기 중"),
    ONGOING("ONGOING", "진행 중"),
    CLOSED("CLOSED", "종료됨");

    private final String key;
    private final String name;
}
