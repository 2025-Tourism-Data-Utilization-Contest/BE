package com.saerok.showing.api.domain.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostVisibility {

    VISIBLE_ALL("VISIBLE_ALL", "전체 공개"),
    TEAM_ONLY("TEAM_ONLY", "팀 전용");

    private final String key;
    private final String name;

    public boolean isTeamOnly() {
        return this == TEAM_ONLY;
    }

    public boolean isVisibleAll() {
        return this == VISIBLE_ALL;
    }
}
