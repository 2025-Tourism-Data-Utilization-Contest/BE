package com.saerok.showing.api.domain.birdTheme.dto.response;

import com.saerok.showing.api.domain.bird.dto.response.BirdSummaryResponse;
import com.saerok.showing.api.domain.birdTheme.entity.BirdTheme;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BirdThemeResponse {

    private Long birdThemeId;

    private BirdSummaryResponse bird;

    public static BirdThemeResponse from(BirdTheme birdTheme) {
        return BirdThemeResponse.builder()
            .birdThemeId(birdTheme.getId())
            .bird(BirdSummaryResponse.toDto(birdTheme.getBird()))
            .build();
    }
}
