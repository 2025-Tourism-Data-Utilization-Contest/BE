package com.saerok.showing.api.domain.bird.dto.response;

import com.saerok.showing.api.domain.bird.entity.Bird;
import com.saerok.showing.api.domain.theme.dto.response.ThemeSummaryResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BirdDetailResponse {

    private Long id;

    private String nameKr;

    private String nameEn;

    private String description;

    private String birdImage;

    private List<ThemeSummaryResponse> themes;

    public static BirdDetailResponse toDto(Bird bird, List<ThemeSummaryResponse> themes) {
        return BirdDetailResponse.builder()
            .id(bird.getId())
            .nameKr(bird.getNameKr())
            .nameEn(bird.getNameEn())
            .description(bird.getDescription())
            .birdImage(bird.getBirdImage())
            .themes(themes)
            .build();
    }
}
