package com.saerok.showing.api.domain.place.dto.response;

import com.saerok.showing.api.domain.place.entity.Place;
import com.saerok.showing.api.domain.theme.dto.response.ThemeSummaryResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDetailResponse {

    private Long id;

    private String title;

    private String placeImage;

    private LocalDate availableFrom;

    private LocalDate availableTo;

    private int price;

    private int reviewCount;

    private List<ThemeSummaryResponse> themes;

    public static PlaceDetailResponse toDto(Place place, int reviewCount, List<ThemeSummaryResponse> themes) {
        return PlaceDetailResponse.builder()
            .id(place.getId())
            .title(place.getTitle())
            .placeImage(place.getPlaceImage())
            .availableFrom(place.getAvailableFrom())
            .availableTo(place.getAvailableTo())
            .price(place.getPrice())
            .reviewCount(reviewCount)
            .themes(themes)
            .build();
    }
}
