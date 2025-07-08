package com.saerok.showing.api.domain.route.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteCreateRequest {

    @NotNull
    @Schema(description = "내 여행코스 이름", example = "5학년 3반 나들이 여행코스")
    private String title;

    @NotNull
    @Schema(description = "여행 시작일(yyyy-MM-dd)", example = "2025-07-08")
    private LocalDate startDate;

    @NotNull
    @Schema(description = "여행 종료일(yyyy-MM-dd)", example = "2025-07-08")
    private LocalDate endDate;

    @Min(1)
    @Max(100)
    @NotNull
    @Schema(description = "인원수, 1~100의 값을 가질 수 있습니다.", example = "15")
    private int peopleCount;
}
