package com.saerok.showing.api.domain.team.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.team.entity.Team;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyTeamResponse {

    private Long teamId;

    private String teamName;

    private boolean leader;

    public static MyTeamResponse toDto(Team team, Member member) {
        return MyTeamResponse.builder()
            .teamId(team.getId())
            .teamName(team.getName())
            .leader(team.isLeader(member))
            .build();
    }
}
