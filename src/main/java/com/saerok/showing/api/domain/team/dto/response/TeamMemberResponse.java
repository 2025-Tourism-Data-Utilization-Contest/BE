package com.saerok.showing.api.domain.team.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamMemberResponse {

    private Long teamId;

    private Long memberId;

    private String name;

    private String profileImage;

    public static TeamMemberResponse toDto(Member member) {
        return TeamMemberResponse.builder()
            .teamId(member.getTeam().getId())
            .memberId(member.getId())
            .name(member.getName())
            .profileImage(member.getProfileImage())
            .build();
    }
}
