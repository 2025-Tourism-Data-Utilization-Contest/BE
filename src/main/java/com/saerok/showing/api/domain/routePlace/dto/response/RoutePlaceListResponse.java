package com.saerok.showing.api.domain.routePlace.dto.response;

import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutePlaceListResponse {

    private List<DayGroupResponse> days;

    public static RoutePlaceListResponse toDto(Route route, List<RoutePlace> routePlaces) {
        // dayNumber 기준 그룹핑 (순서 유지)
        Map<Integer, List<RoutePlace>> grouped = routePlaces.stream()
            .collect(Collectors.groupingBy(
                RoutePlace::getDayNumber,
                LinkedHashMap::new,
                Collectors.toList()
            ));
        // 그룹 데이터를 DayGroupResponse 리스트로 변환
        List<DayGroupResponse> days = grouped.entrySet().stream()
            .map(entry -> {
                int day = entry.getKey();
                LocalDate visitDate = route.getStartDate().plusDays(day - 1);
                List<RoutePlaceBoxResponse> summaries = entry.getValue().stream()
                    .map(RoutePlaceBoxResponse::toDto)
                    .toList();
                return new DayGroupResponse(day, visitDate, summaries);
            })
            .toList();

        return RoutePlaceListResponse.builder().days(days).build();
    }
}
