package com.saerok.showing.api.domain.theme.dto.response;

import com.saerok.showing.api.domain.theme.entity.DayTime;
import com.saerok.showing.api.domain.theme.entity.Season;
import com.saerok.showing.api.domain.theme.entity.Theme;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThemePinResponse {

    private Long id;

    private List<Season> seasons;

    private List<DayTime> dayTimes;

    private Double locationX;

    private Double locationY;

    public static ThemePinResponse toDto(Theme theme) {
        return ThemePinResponse.builder()
            .id(theme.getId())
            .seasons(theme.getSeasons())
            .dayTimes(theme.getDayTimes())
            .locationX(theme.getLocationX())
            .locationY(theme.getLocationY())
            .build();
    }
}
