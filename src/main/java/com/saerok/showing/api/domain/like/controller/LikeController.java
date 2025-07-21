package com.saerok.showing.api.domain.like.controller;

import com.saerok.showing.api.domain.like.dto.request.LikeToggleRequest;
import com.saerok.showing.api.domain.like.service.LikeService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
@Tag(name = "PostLike", description = "좋아요 관리")
public class LikeController {

    private final LikeService likeService;

    @Operation(
        summary = "좋아요 토글 (게시글/댓글)",
        description = """
            [모든 Role 가능] 좋아요 토글입니다.<br>
            좋아요 대상은 POST(게시글), COMMENT(댓글) 2가지입니다.<br>
            좋아요 된 대상을 다시 토글하게 되면 좋아요가 취소됩니다.<br>
            true 반환인 경우 좋아요 등록, false 반환은 좋아요 취소입니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping
    public ApiResponse<Boolean> toggleLike(@Valid @RequestBody LikeToggleRequest request) {
        boolean liked = likeService.toggleLike(request);
        return ApiResponse.success(liked);
    }
}
