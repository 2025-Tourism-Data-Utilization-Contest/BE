package com.saerok.showing.api.domain.theme.dto.response;

import com.saerok.showing.api.domain.bird.dto.response.BirdSummaryResponse;
import com.saerok.showing.api.domain.place.dto.response.AttractionPlaceSummaryResponse;
import com.saerok.showing.api.domain.place.dto.response.ExperiencePlaceSummaryResponse;
import com.saerok.showing.api.domain.review.dto.response.ReviewSummaryResponse;
import com.saerok.showing.api.domain.theme.entity.Theme;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThemeDetailResponse {

    private Long id;

    private String title;

    private String description;

    private String themeImage;

    private List<BirdSummaryResponse> birds;

    private List<AttractionPlaceSummaryResponse> attractionPlaces;

    private List<ExperiencePlaceSummaryResponse> experiencePlaces;

    private int reviewCount;

    private List<ReviewSummaryResponse> reviews;

    public static ThemeDetailResponse toDto(
        Theme theme,
        List<BirdSummaryResponse> birds,
        List<AttractionPlaceSummaryResponse> attractionPlaces,
        List<ExperiencePlaceSummaryResponse> experiencePlaces,
        int reviewCount,
        List<ReviewSummaryResponse> reviews
    ) {
        return ThemeDetailResponse.builder()
            .id(theme.getId())
            .title(theme.getTitle())
            .description(theme.getDescription())
            .themeImage(theme.getThemeImage())
            .birds(birds)
            .attractionPlaces(attractionPlaces)
            .experiencePlaces(experiencePlaces)
            .reviewCount(reviewCount)
            .reviews(reviews)
            .build();
    }
}
