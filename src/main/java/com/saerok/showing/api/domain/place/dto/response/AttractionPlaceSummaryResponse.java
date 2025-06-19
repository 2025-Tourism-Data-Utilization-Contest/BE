package com.saerok.showing.api.domain.place.dto.response;

import com.saerok.showing.api.domain.place.entity.Place;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttractionPlaceSummaryResponse {

    private Long id;

    private String title;

    private String placeImage;

    public static AttractionPlaceSummaryResponse toDto(Place place) {
        return AttractionPlaceSummaryResponse.builder()
            .id(place.getId())
            .title(place.getTitle())
            .placeImage(place.getPlaceImage())
            .build();
    }
}
