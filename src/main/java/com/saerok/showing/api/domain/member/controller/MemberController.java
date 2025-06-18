package com.saerok.showing.api.domain.member.controller;

import com.saerok.showing.api.domain.member.dto.request.MemberUpdateRequest;
import com.saerok.showing.api.domain.member.dto.response.MyPageResponse;
import com.saerok.showing.api.domain.member.service.MemberService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 관리")
public class MemberController {

    private final MemberService memberService;

    @Operation(
        summary = "내 정보 조회 (마이페이지)",
        description = "[모든 Role 가능] 현재 로그인한 사용자의 정보를 반환합니다."
    )
    @GetMapping("/me")
    public ApiResponse<MyPageResponse> getMember() {
        MyPageResponse response = memberService.getMember();
        return ApiResponse.success(response);
    }

    @Operation(
        summary = "회원 수정",
        description = "[모든 Role 가능] 회원의 이름과 프로필 이미지를 수정할 수 있습니다."
    )
    @PatchMapping("")
    public ApiResponse<Long> update(
        @Valid @RequestBody MemberUpdateRequest request
    ) {
        Long id = memberService.update(request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "회원 삭제",
        description = "[모든 Role 가능]"
    )
    @DeleteMapping("/{memberId}")
    public ApiResponse<Long> delete(
        @PathVariable(name = "memberId") Long memberId
    ) {
        Long id = memberService.delete(memberId);
        return ApiResponse.success(id);
    }
}
