package com.saerok.showing.api.domain.post.controller;

import com.saerok.showing.api.domain.post.service.PostLikeService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/{postId}/likes")
@Tag(name = "PostLike", description = "게시글 좋아요 관리")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(summary = "좋아요 토글")
    @PostMapping
    public ApiResponse<Boolean> toggleLike(@PathVariable Long postId) {
        boolean liked = postLikeService.toggleLike(postId);
        return ApiResponse.success(liked); // true: 좋아요됨 / false: 좋아요 취소됨
    }
}
