package com.saerok.showing.api.domain.poll.controller;

import com.saerok.showing.api.domain.poll.dto.request.PollCandidateRegisterRequest;
import com.saerok.showing.api.domain.poll.dto.request.PollCreateRequest;
import com.saerok.showing.api.domain.poll.dto.request.PollUpdateRequest;
import com.saerok.showing.api.domain.poll.dto.response.PollDetailResponse;
import com.saerok.showing.api.domain.poll.service.PollService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/poll")
@RequiredArgsConstructor
@Tag(name = "Poll", description = "투표 관리")
public class PollController {

    private final PollService pollService;

    @Operation(
        summary = "투표 등록",
        description = """
            [모든 Role 가능] 투표를 등록합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("")
    public ApiResponse<Long> createPoll(
        @Valid @RequestBody PollCreateRequest request
    ) {
        Long id = pollService.save(request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "투표 후보 조회",
        description = "[모든 Role 가능] 투표에 등록된 Route 후보 리스트를 조회합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{pollId}")
    public ApiResponse<PollDetailResponse> getPoll(
        @PathVariable(name = "pollId") Long pollId
    ) {
        PollDetailResponse response = pollService.getPoll(pollId);
        return ApiResponse.success(response);
    }

    @Operation(
        summary = "투표 후보 등록",
        description = """
            [모든 Role 가능] 투표에 여행 경로(Route)를 하나 등록합니다.<br>
            자신이 생성한 여행 경로 중 하나를 투표 후보로 올릴 수 있으며,<br>
            동일한 경로는 중복 등록할 수 없습니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{pollId}/candidates")
    public ApiResponse<Void> registerCandidate(
        @PathVariable Long pollId,
        @Valid @RequestBody PollCandidateRegisterRequest request
    ) {
        pollService.registerCandidate(pollId, request);
        return ApiResponse.success();
    }

    @Operation(
        summary = "투표 수정",
        description = "[모든 Role 가능] 투표 ID를 기준으로 내용 및 투표 상태를 수정합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("/{pollId}")
    public ApiResponse<Long> update(
        @PathVariable(name = "pollId") Long pollId,
        @Valid @RequestBody PollUpdateRequest request
    ) {
        Long id = pollService.update(pollId, request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "투표 삭제",
        description = "[모든 Role 가능] 투표 ID를 기준으로 투표를 삭제합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{pollId}")
    public ApiResponse<Long> delete(
        @PathVariable(name = "pollId") Long pollId
    ) {
        Long id = pollService.delete(pollId);
        return ApiResponse.success(id);
    }

}
