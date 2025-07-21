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
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Comment", description = "댓글 관리")
public class CommentController {

    private final CommentService commentService;

    @Operation(
        summary = "댓글 등록",
        description = """
            [모든 Role 가능] 댓글을 등록합니다.<br>
            `postId`에 해당하는 게시글에 대해 댓글이 작성됩니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("")
    public ApiResponse<Long> createComment(
        @PathVariable Long postId,
        @Valid @RequestBody CommentCreateRequest request
    ) {
        Long commentId = commentService.create(postId, request);
        return ApiResponse.success(commentId);
    }

    @Operation(
        summary = "댓글 목록 조회",
        description = """
            [모든 Role 가능] 특정 게시글에 작성된 댓글 목록을 조회합니다.<br>
            댓글은 최신순으로 정렬되어 반환됩니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("")
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long postId) {
        List<CommentResponse> responses = commentService.getCommentsByPostId(postId);
        return ApiResponse.success(responses);
    }

    @Operation(
        summary = "댓글 삭제",
        description = """
            [모든 Role 가능] 사용자가 작성한 댓글을 삭제합니다.<br>
            자신의 댓글만 삭제할 수 있으며, 게시글 ID(`postId`)와 댓글 ID(`commentId`)를 함께 전달해야 합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{commentId}")
    public ApiResponse<Long> delete(
        @PathVariable Long postId,
        @PathVariable Long commentId
    ) {
        Long id = commentService.delete(postId, commentId);
        return ApiResponse.success(id);
    }
}
