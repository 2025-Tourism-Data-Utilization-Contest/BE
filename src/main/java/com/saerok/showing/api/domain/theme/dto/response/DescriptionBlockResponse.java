package com.saerok.showing.api.domain.theme.dto.response;

import com.saerok.showing.api.domain.theme.entity.DescriptionBlock;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DescriptionBlockResponse {

    private String title;

    private String description;

    public static DescriptionBlockResponse toDto(DescriptionBlock descriptionBlock) {
        return DescriptionBlockResponse.builder()
            .title(descriptionBlock.getTitle())
            .description(descriptionBlock.getDescription())
            .build();
    }
}
