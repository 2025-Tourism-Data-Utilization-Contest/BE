package com.saerok.showing.api.domain.review.controller;

import com.saerok.showing.api.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관리")
public class ReviewController {

    private final ReviewService reviewService;
}
