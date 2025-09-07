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

    private String contentId;

    private String contentTypeId;

    private String imageUrl;

    private Double latitude;

    private Double longitude;

    private String address;

    public static RoutePlaceBoxResponse toDto(RoutePlace routePlace) {
        return RoutePlaceBoxResponse.builder()
            .title(routePlace.getPlaceName())
            .dayNumber(routePlace.getDayNumber())
            .orderInDay(routePlace.getOrderInDay())
            .contentId(routePlace.getContentId())
            .contentTypeId(routePlace.getContentTypeId())
            .imageUrl(routePlace.getImageUrl())
            .latitude(routePlace.getLatitude())
            .longitude(routePlace.getLongitude())
            .address(routePlace.getAddress())
            .build();
    }
}
