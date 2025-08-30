package com.saerok.showing.api.domain.routePlace.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoutePlaceCreateRequest {

    @NotNull
    @Schema(description = "추가할 장소의 이름", example = "엘리시안 강촌 리조트")
    private String placeName;

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "여행 경로에서 해당 장소를 방문할 일자(Day N)입니다, N은 1~30의 값입니다.", example = "1")
    private int dayNumber;

    @Min(1)
    @Max(30)
    @NotNull
    @Schema(description = "해당 일자 내 장소 방문 순서(M번쨰)입니다. M은 1~30의 값입니다.", example = "1")
    private int orderInDay;

    @NotNull
    @Schema(description = "TourAPI 조회용 pk값 1", example = "1")
    private String contentId;

    @NotNull
    @Schema(description = "TourAPI 조회용 pk값 2", example = "1")
    private String contentTypeId;

    @Schema(description = "TourAPI 이미지 썸네일 조회용 url", example = "")
    private String imageUrl;
}
