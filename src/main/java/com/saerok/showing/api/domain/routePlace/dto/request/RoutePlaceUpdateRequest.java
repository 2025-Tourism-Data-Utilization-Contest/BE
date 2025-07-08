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
    @Schema(description = "여행 경로에서 해당 장소의 방문 일자(Day N)를 수정합니다, N은 1~30의 값입니다.", example = "1")
    private int dayNumber;

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "해당 일자 내에서 장소 방문 순서를 수정합니다. 1~30의 값을 가집니다.", example = "1")
    private int orderInDay;
}
