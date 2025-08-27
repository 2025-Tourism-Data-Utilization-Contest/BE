package com.saerok.showing.api.domain.route.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteUpdateRequest {

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "내 여행코스 이름, 여행코스는 1자 이상 50자 이하여야 합니다.", example = "5학년 3반 끝내주는 피크닉 코스")
    private String title;
}
