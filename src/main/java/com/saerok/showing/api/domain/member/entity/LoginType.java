package com.saerok.showing.api.domain.member.entity;

import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoginType {

    LOCAL("LOCAL", "일반 로그인"),
    NAVER("NAVER", "네이버 로그인"),
    KAKAO("KAKAO", "카카오 로그인");

    private final String key;          // 로그인 타입 키 (예: NAVER, KAKAO)
    private final String description;  // 설명 (예: 네이버 로그인, 카카오 로그인)

    public static LoginType from(String provider) {
        return Arrays.stream(LoginType.values())
            .filter(type -> type.name().equalsIgnoreCase(provider))
            .findFirst()
            .orElseThrow(() -> ShowingException.from(ErrorCode.INVALID_SOCIAL_TYPE));
    }
}
