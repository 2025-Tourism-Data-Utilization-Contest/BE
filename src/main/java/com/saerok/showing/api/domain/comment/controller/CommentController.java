package com.saerok.showing.api.domain.comment.controller;

import com.saerok.showing.api.domain.comment.dto.request.CommentCreateRequest;
import com.saerok.showing.api.domain.comment.dto.response.CommentResponse;
import com.saerok.showing.api.domain.comment.service.CommentService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "PostComment", description = "게시글 댓글 관리")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 등록")
    @PostMapping("")
    public ApiResponse<Long> createComment(
        @PathVariable Long postId,
        @Valid @RequestBody CommentCreateRequest request
    ) {
        Long commentId = commentService.create(postId, request);
        return ApiResponse.success(commentId);
    }

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        List<CommentResponse> responses = commentService.getCommentsByPostId(postId);
        return ApiResponse.success(responses);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.delete(postId, commentId);
        return ApiResponse.success(null);
    }
}
