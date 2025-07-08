package com.saerok.showing.api.domain.routePlace.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DayGroupResponse {

    private int dayNumber;

    private LocalDate visitDate;

    private List<RoutePlaceBoxResponse> places;
}
