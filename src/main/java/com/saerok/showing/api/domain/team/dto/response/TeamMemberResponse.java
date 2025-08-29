package com.saerok.showing.api.domain.team.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamMemberResponse {

    private Long teamId;

    private Long memberId;

    private String memberName;

    private String profileImage;

    public static TeamMemberResponse toDto(Member member, Long teamId) {
        return TeamMemberResponse.builder()
            .teamId(teamId)
            .memberId(member.getId())
            .memberName(member.getName())
            .profileImage(member.getProfileImage())
            .build();
    }
}
