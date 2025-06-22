package com.saerok.showing.api.domain.review.dto.request;

import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCreateRequest {

    @NotNull
    @Schema(description = "타겟Id", example = "1")
    private Long targetId;

    @NotNull
    @Schema(description = "타겟타입", example = "THEME")
    private ReviewTargetType targetType;

    @NotNull
    @Min(value = 1, message = "별점은 최소 1점 이상이어야 합니다.")
    @Max(value = 5, message = "별점은 최대 5점까지 가능합니다.")
    @Schema(description = "리뷰 별점", example = "5")
    private int rating;

    @NotNull
    @Size(max = 1000, message = "리뷰 코멘트는 최대 1000자까지 입력할 수 있습니다.")
    @Schema(description = "리뷰 내용", example = "순천만 습지 처음 와봤는데 너무 재밌었어요~!")
    private String comment;
}
