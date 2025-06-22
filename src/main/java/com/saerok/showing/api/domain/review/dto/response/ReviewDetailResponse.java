package com.saerok.showing.api.domain.review.dto.response;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.review.entity.Review;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDetailResponse {

    private Long id;

    private String memberName;

    private ReviewTargetType targetType;

    private Long targetId;

    private int rating;

    private String comment;

    private LocalDateTime updatedAt;

    public static ReviewDetailResponse toDto(Review review, Member member) {
        return ReviewDetailResponse.builder()
            .id(review.getId())
            .memberName(review.getMember().getName())
            .targetType(review.getTargetType())
            .targetId(review.getTargetId())
            .rating(review.getRating())
            .comment(review.getComment())
            .updatedAt(review.getUpdatedAt())
            .build();
    }
}
