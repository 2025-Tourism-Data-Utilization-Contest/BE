package com.saerok.showing.api.domain.bird.dto.response;

import com.saerok.showing.api.domain.bird.entity.Bird;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BirdSummaryResponse {

    private Long id;

    private String name;

    public static BirdSummaryResponse toDto(Bird bird) {
        return BirdSummaryResponse.builder()
            .id(bird.getId())
            .name(bird.getNameKr())
            .build();
    }
}
