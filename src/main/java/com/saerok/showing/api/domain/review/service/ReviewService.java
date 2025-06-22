package com.saerok.showing.api.domain.review.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.review.dto.request.ReviewCreateRequest;
import com.saerok.showing.api.domain.review.dto.request.ReviewUpdateRequest;
import com.saerok.showing.api.domain.review.dto.response.ReviewDetailResponse;
import com.saerok.showing.api.domain.review.dto.response.ReviewSummaryResponse;
import com.saerok.showing.api.domain.review.entity.Review;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import com.saerok.showing.api.domain.review.repository.ReviewRepository;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long save(ReviewCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Review review = Review.toEntity(member, request);
        reviewRepository.save(review);
        return null;
    }

    @Transactional(readOnly = true)
    public List<ReviewSummaryResponse> getAllReviews(ReviewTargetType targetType, Long targetId) {
        return reviewRepository.findByTargetTypeAndTargetId(targetType, targetId).stream()
            .map(ReviewSummaryResponse::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReviewDetailResponse getReview(Long reviewId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Review review = findById(reviewId);
        return ReviewDetailResponse.toDto(review, member);
    }

    @Transactional
    public Long update(Long reviewId, ReviewUpdateRequest request) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Review review = findById(reviewId);
        review.validateOwner(currentMember);
        review.update(request);
        return review.getId();
    }

    @Transactional
    public Long delete(Long reviewId) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Review review = findById(reviewId);
        review.validateOwner(currentMember);
        reviewRepository.deleteById(reviewId);
        return reviewId;
    }

    @Transactional(readOnly = true)
    public double getAverageRating(ReviewTargetType targetType, Long targetId) {
        Double average = reviewRepository.findAverageRatingByTarget(targetType, targetId);
        return average != null ? Math.round(average * 10.0) / 10.0 : 0.0;
    }

    public int getReviewCount(ReviewTargetType targetType, Long targetId) {
        return reviewRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }

    public List<ReviewSummaryResponse> getRecentReviews(ReviewTargetType targetType, Long targetId) {
        return reviewRepository.findTop5ByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId)
            .stream()
            .map(ReviewSummaryResponse::toDto)
            .collect(Collectors.toList());
    }

    private Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.REVIEW_NOT_FOUND));
    }
}
