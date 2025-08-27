package com.saerok.showing.api.domain.routePlace.dto.response;

import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoutePlaceBoxResponse {

    private String title;

    private int dayNumber;

    private int orderInDay;

//    private String imageUrl;

    public static RoutePlaceBoxResponse toDto(RoutePlace routePlace) {
        return RoutePlaceBoxResponse.builder()
            .title(routePlace.getPlaceName())
            .dayNumber(routePlace.getDayNumber())
            .orderInDay(routePlace.getOrderInDay())
//            .imageUrl(routePlace.getPlace().getPlaceImage())
            .build();
    }
}
