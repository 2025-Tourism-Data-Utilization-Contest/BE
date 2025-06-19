package com.saerok.showing.api.domain.theme.dto.response;

import com.saerok.showing.api.domain.bird.dto.response.BirdSummaryResponse;
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

    // private List<PlaceSummaryResponse> places;

    public static ThemeDetailResponse toDto(Theme theme, List<BirdSummaryResponse> birds) {
        return ThemeDetailResponse.builder()
            .id(theme.getId())
            .title(theme.getTitle())
            .description(theme.getDescription())
            .themeImage(theme.getThemeImage())
            .birds(birds)
            .build();
    }
}
