package com.saerok.showing.api.domain.place.dto.response;

import com.saerok.showing.api.domain.place.entity.Place;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExperiencePlaceSummaryResponse {

    private Long id;

    private String title;

    private String placeImage;

    private LocalDate availableFrom;

    private LocalDate availableTo;

    private int price;

    public static ExperiencePlaceSummaryResponse toDto(Place place) {
        return ExperiencePlaceSummaryResponse.builder()
            .id(place.getId())
            .title(place.getTitle())
            .placeImage(place.getPlaceImage())
            .availableFrom(place.getAvailableFrom())
            .availableTo(place.getAvailableTo())
            .price(place.getPrice())
            .build();
    }
}
