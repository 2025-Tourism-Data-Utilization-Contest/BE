package com.saerok.showing.api.domain.route.dto.response;

import com.saerok.showing.api.domain.route.entity.Route;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteResponse {

    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer peopleCount;

    public static RouteResponse toDto(Route route) {
        return RouteResponse.builder()
            .id(route.getId())
            .title(route.getTitle())
            .startDate(route.getStartDate())
            .endDate(route.getEndDate())
            .peopleCount(route.getPeopleCount())
            .build();
    }
}
