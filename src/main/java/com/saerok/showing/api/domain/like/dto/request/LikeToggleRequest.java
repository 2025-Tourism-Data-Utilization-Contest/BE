package com.saerok.showing.api.domain.like.dto.request;

import com.saerok.showing.api.domain.like.entity.LikeTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeToggleRequest {

    @NotNull
    @Schema(description = "좋아요 대상 id", example = "1")
    private Long targetId;

    @NotNull
    @Schema(description = "좋아요 대상 타입", example = "POST")
    private LikeTargetType likeTargetType;
}
