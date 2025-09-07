package com.saerok.showing.api.domain.poll.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.poll.entity.Poll;
import com.saerok.showing.api.domain.poll.entity.PollStatus;
import com.saerok.showing.api.domain.route.dto.response.RouteSummaryResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PollDetailResponse {

    private Long id;

    private String writer;

    private String writerProfileImage;

    private String title;

    private PollStatus pollStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    private int commentCount;

    private List<RouteSummaryResponse> routes;

    private Long teamId;

    private boolean isLiked;

    public static PollDetailResponse toDto(
        Poll poll,
        Member member,
        int commentCount,
        List<RouteSummaryResponse> routes,
        boolean isLiked
    ) {
        return PollDetailResponse.builder()
            .id(poll.getId())
            .writer(member.getName())
            .writerProfileImage(member.getProfileImage())
            .title(poll.getTitle())
            .pollStatus(poll.getPollStatus())
            .startDate(poll.getStartDate())
            .endDate(poll.getEndDate())
            .commentCount(commentCount)
            .routes(routes)
            .teamId(poll.getTeam().getId())
            .isLiked(isLiked)
            .build();
    }
}
