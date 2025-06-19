package com.saerok.showing.api.domain.theme.dto.response;

import com.saerok.showing.api.domain.theme.entity.Theme;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThemeSummaryResponse {

    private Long id;

    private String title;

    public static ThemeSummaryResponse toDto(Theme theme) {
        return ThemeSummaryResponse.builder()
            .id(theme.getId())
            .title(theme.getTitle())
            .build();
    }
}
