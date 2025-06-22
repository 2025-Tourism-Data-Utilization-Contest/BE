package com.saerok.showing.api.domain.review.controller;

import com.saerok.showing.api.domain.review.dto.request.ReviewCreateRequest;
import com.saerok.showing.api.domain.review.dto.request.ReviewUpdateRequest;
import com.saerok.showing.api.domain.review.dto.response.ReviewDetailResponse;
import com.saerok.showing.api.domain.review.dto.response.ReviewSummaryResponse;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import com.saerok.showing.api.domain.review.service.ReviewService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관리")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
        summary = "리뷰 등록",
        description = """
            [모든 Role 가능] 테마 또는 장소에 대해 리뷰를 작성합니다.<br>
            FE에서 처리한 targetId와 targetType을 포함하여 요청합니다.<br>
            Rating은 1~5사이의 정수입니다.<br>
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("")
    public ApiResponse<Long> createReview(
        @Valid @RequestBody ReviewCreateRequest reviewCreateRequest
    ) {
        Long id = reviewService.save(reviewCreateRequest);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "리뷰 상세 조회",
        description = "[모든 Role 가능] 리뷰 ID로 리뷰 상세 정보를 조회합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewDetailResponse> getReview(
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        ReviewDetailResponse response = reviewService.getReview(reviewId);
        return ApiResponse.success(response);
    }

    @Operation(
        summary = "리뷰 목록 조회",
        description = """
        [모든 Role 가능] 특정 대상(THEME 또는 PLACE)에 대한 모든 리뷰를 조회합니다.<br>
        - `reviewTargetType` : 리뷰 대상의 타입 (예: THEME, PLACE)
        - `targetId` : 대상의 고유 ID (예: 테마 ID 또는 장소 ID)
        """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/list")
    public ApiResponse<List<ReviewSummaryResponse>> getAllReviews(
        @RequestParam(name = "reviewTargetType") ReviewTargetType reviewTargetType,
        @RequestParam(name = "targetId") Long targetId
    ) {
        List<ReviewSummaryResponse> response = reviewService.getAllReviews(reviewTargetType, targetId);
        return ApiResponse.success(response);
    }

    @Operation(
        summary = "리뷰 수정",
        description = "[모든 Role 가능] 리뷰 ID를 기준으로 내용을 수정합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("/{reviewId}")
    public ApiResponse<Long> update(
        @PathVariable(name = "reviewId") Long reviewId,
        @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest
    ) {
        Long id = reviewService.update(reviewId, reviewUpdateRequest);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "리뷰 삭제",
        description = "[모든 Role 가능] 리뷰 ID를 기준으로 해당 리뷰를 삭제합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{reviewId}")
    public ApiResponse<Long> delete(
        @PathVariable(name = "reviewId") Long reviewId
    ) {
        Long id = reviewService.delete(reviewId);
        return ApiResponse.success(id);
    }
}
