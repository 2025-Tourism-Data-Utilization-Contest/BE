package com.saerok.showing.api.domain.bird.controller;

import com.saerok.showing.api.domain.bird.dto.response.BirdDetailResponse;
import com.saerok.showing.api.domain.bird.service.BirdService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bird")
@RequiredArgsConstructor
@Tag(name = "Bird", description = "새 관리")
public class BirdController {

    private final BirdService birdService;

    @Operation(
        summary = "새 단건 조회",
        description = "[모든 Role 가능] birdId로 새의 상세 정보를 조회합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{birdId}")
    public ApiResponse<BirdDetailResponse> getBird(@PathVariable Long birdId) {
        BirdDetailResponse bird = birdService.getBird(birdId);
        return ApiResponse.success(bird);
    }
}
