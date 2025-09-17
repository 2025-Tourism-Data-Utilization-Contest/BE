package com.saerok.showing.api.global.auth.dto;

import com.saerok.showing.api.domain.member.entity.LoginType;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuestLoginResponse {

    Long memberId;

    String email;

    String name;

    String nickName;

    Role role;

    LoginType loginType;

    public static GuestLoginResponse toDto(Member member) {
        return GuestLoginResponse.builder()
            .memberId(member.getId())
            .email(member.getEmail())
            .name(member.getName())
            .nickName(member.getNickname())
            .role(member.getRole())
            .loginType(member.getLoginType())
            .build();
    }
}
