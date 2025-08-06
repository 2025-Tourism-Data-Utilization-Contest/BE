package com.saerok.showing.api.domain.post.controller;

import com.saerok.showing.api.domain.post.dto.request.PostCreateRequest;
import com.saerok.showing.api.domain.post.dto.request.PostUpdateRequest;
import com.saerok.showing.api.domain.post.dto.response.PostDetailResponse;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.domain.post.service.PostService;
import com.saerok.showing.api.global.pagination.cursorResult.CursorResult;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관리")
public class PostController {

    private final PostService postService;

    @Operation(
        summary = "게시글 등록",
        description = """
            [모든 Role 가능] 게시글을 작성합니다.<br>
            요청 본문에는 제목, 내용, 게시글 타입, 게시글 이미지(리스트), 해시태그(리스트)가 포함됩니다.<br>
            게시글 이미지는 "/api/v1/file/post"를 이용하여 얻은 fileUrl값들을 입력해주세요.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("")
    public ApiResponse<Long> createPost(
        @Valid @RequestBody PostCreateRequest request
    ) {
        Long id = postService.save(request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "게시글 단건 조회",
        description = "[모든 Role 가능] 게시글 ID로 하나의 게시글의 상세 정보를 조회합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetails(
        @PathVariable(name = "postId") Long postId
    ) {
        PostDetailResponse response = postService.getPostDetails(postId);
        return ApiResponse.success(response);
    }

    @Operation(
        summary = "게시글 전체 조회",
        description = """
            [모든 Role 가능] 게시글 타입에 따른 카테고리별 게시글을 조회합니다.<br>
            - 게시글 타입: 일반(NORMAL), 투표(POLL), 여행계획(ROUTE)<br>
            - `postType`을 생략하면 전체 게시글이 조회됩니다.<br>
            - 정렬 타입: 최신순(LATEST), 인기순(POPULAR)<br>
            - `sort`을 생략하면 최신순으로 정렬됩니다.<br>
            
            📌 커서 기반 페이지네이션 안내<br>
            - `cursorRaw` : 마지막으로 조회된 게시글의 `createdAt` 값입니다. 이후의 데이터를 조회할 때 사용됩니다.<br>
            - `limit` : 한 번에 가져올 데이터 수입니다. 기본값은 4이며, 무한 스크롤에 사용됩니다.<br>
            - 응답에는 다음 페이지 존재 여부(`hasNext`) 및 이전 페이지 존재 여부(`hasPrevious`)가 함께 포함됩니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/list")
    public ApiResponse<CursorResult<PostSummaryResponse>> getPosts(
        @RequestParam(name = "postType", required = false) PostType postType,
        @RequestParam(name = "sort", required = false, defaultValue = "LATEST") PostSortType sortType,
        @RequestParam(name = "cursor", required = false) String cursorRaw,
        @RequestParam(name = "limit", defaultValue = "4") int limit
    ) {
        CursorResult<PostSummaryResponse> result = postService.getPosts(postType, sortType, cursorRaw, limit);
        return ApiResponse.success(result);
    }

    @Operation(
        summary = "게시글 수정",
        description = "[모든 Role 가능] 게시글 ID를 기준으로 내용을 수정합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("/{postId}")
    public ApiResponse<Long> update(
        @PathVariable(name = "postId") Long postId,
        @Valid @RequestBody PostUpdateRequest request
    ) {
        Long id = postService.update(postId, request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "게시글 삭제",
        description = "[모든 Role 가능] 게시글 ID를 기준으로 해당 게시글을 삭제합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{postId}")
    public ApiResponse<Long> delete(
        @PathVariable(name = "postId") Long postId
    ) {
        Long id = postService.delete(postId);
        return ApiResponse.success(id);
    }
}
