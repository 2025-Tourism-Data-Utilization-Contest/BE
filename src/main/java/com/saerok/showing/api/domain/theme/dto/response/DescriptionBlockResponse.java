package com.saerok.showing.api.domain.theme.dto.response;

import com.saerok.showing.api.domain.theme.entity.DescriptionBlock;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DescriptionBlockResponse {

    private String title;
    private String description;

    public static DescriptionBlockResponse from(DescriptionBlock block) {
        return DescriptionBlockResponse.builder()
            .title(block.getTitle())
            .description(block.getDescription())
            .build();
    }

    public static List<DescriptionBlockResponse> fromList(List<DescriptionBlock> blocks) {
        return blocks.stream()
            .map(DescriptionBlockResponse::from)
            .toList();
    }
}
