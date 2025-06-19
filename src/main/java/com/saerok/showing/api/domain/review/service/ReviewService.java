package com.saerok.showing.api.domain.review.service;

import com.saerok.showing.api.domain.review.dto.response.ReviewSummaryResponse;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import com.saerok.showing.api.domain.review.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public int getReviewCount(ReviewTargetType targetType, Long targetId) {
        return reviewRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }

    public List<ReviewSummaryResponse> getRecentReviews(ReviewTargetType targetType, Long targetId) {
        return reviewRepository.findTop5ByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId)
            .stream()
            .map(ReviewSummaryResponse::toDto)
            .collect(Collectors.toList());
    }
}
