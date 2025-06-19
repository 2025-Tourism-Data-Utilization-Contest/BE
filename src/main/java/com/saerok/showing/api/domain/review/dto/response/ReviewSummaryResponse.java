package com.saerok.showing.api.domain.review.dto.response;

import com.saerok.showing.api.domain.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewSummaryResponse {

    private Long id;

    private String comment;

    public static ReviewSummaryResponse toDto(Review review) {
        return ReviewSummaryResponse.builder()
            .id(review.getId())
            .comment(review.getComment())
            .build();
    }
}
