package com.saerok.showing.api.domain.place.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceSummaryResponse {

    private String title;

    public static PlaceSummaryResponse create(String placeName) {
        return PlaceSummaryResponse.builder()
            .title(placeName)
            .build();
    }
}
