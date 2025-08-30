package com.saerok.showing.api.domain.routePlace.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutePlaceUpdateRequest {

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "기존 일차(이동 전)", example = "1")
    private Integer oldDayNumber;

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "기존 순서(이동 전)", example = "2")
    private Integer oldOrderInDay;

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "새 일차(이동 후)", example = "1")
    private Integer dayNumber;

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "새 순서(이동 후)", example = "1")
    private Integer orderInDay;

    @Schema(description = "장소 이름", example = "엘리시안 강촌 리조트")
    private String placeName;
}
