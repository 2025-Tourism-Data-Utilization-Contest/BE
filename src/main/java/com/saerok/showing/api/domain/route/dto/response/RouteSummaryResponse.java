package com.saerok.showing.api.domain.route.dto.response;

import com.saerok.showing.api.domain.place.dto.response.PlaceSummaryResponse;
import com.saerok.showing.api.domain.route.entity.Route;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteSummaryResponse {

    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<PlaceSummaryResponse> placeSummaries;

    private int likeCount;

    public static RouteSummaryResponse toDto(Route route, List<PlaceSummaryResponse> placeSummaries) {
        return RouteSummaryResponse.builder()
            .id(route.getId())
            .title(route.getTitle())
            .startDate(route.getStartDate())
            .endDate(route.getEndDate())
            .placeSummaries(placeSummaries)
            .likeCount(route.getLikeCount())
            .build();
    }
}
