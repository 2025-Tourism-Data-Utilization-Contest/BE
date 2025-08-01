package com.saerok.showing.api.domain.team.controller;

import com.saerok.showing.api.domain.team.dto.request.TeamCreateRequest;
import com.saerok.showing.api.domain.team.dto.request.TeamJoinRequest;
import com.saerok.showing.api.domain.team.dto.response.TeamMemberResponse;
import com.saerok.showing.api.domain.team.service.TeamService;
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
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
@Tag(name = "Team", description = "팀 생성/삭제")
public class TeamController {

    private final TeamService teamService;

    @Operation(
        summary = "팀 생성",
        description = """
            [모든 Role 가능] 새로운 팀을 생성합니다.<br>
            팀을 생성한 사람은 팀리더가 됩니다.<br>
            이미 팀이 존재하는 회원은 팀 생성을 할 수 없습니다.<br>
            팀명은 2~30글자 이며 중복이 불가능합니다.<br>
            팀 인증코드는 영문자와 숫자를 조합한 4~30글자입니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("")
    public ApiResponse<Long> createTeam(
        @Valid @RequestBody TeamCreateRequest request
    ) {
        Long id = teamService.save(request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "팀 가입",
        description = """
            [모든 Role 가능] 팀에 가입합니다.<br>
            이미 팀이 존재하거나 요청한 팀에 가입된 경우는 가입되지 않습니다.
            입장할 팁의 아이디와 비밀번호가 필요합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/join")
    public ApiResponse<Long> joinTeam(
        @Valid @RequestBody TeamJoinRequest request
    ) {
        Long id = teamService.joinTeam(request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "팀 탈퇴",
        description = """
            [모든 Role 가능] 현재 로그인한 사용자가 팀에서 탈퇴합니다.<br>
            팀의 소속인 회원만 탈퇴할 수 있습니다.<br>
            팀의 리더가 탈퇴하는 경우, 해당 팀이 사라집니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{teamId}/leave")
    public ApiResponse<Long> leaveTeam(
        @PathVariable(name = "teamId") Long teamId
    ) {
        Long id = teamService.leaveTeam(teamId);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "팀 내 팀원 목록 조회",
        description = """
            [모든 Role 가능] 팀에 속한 팀원들을 조회합니다.<br>
            먼저 가입한 순으로 정렬되며, 팀리더가 제일먼저입니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/members")
    public ApiResponse<List<TeamMemberResponse>> getMyTeamMembers() {
        List<TeamMemberResponse> members = teamService.getMyTeamMembers();
        return ApiResponse.success(members);
    }

    @Operation(
        summary = "팀 삭제",
        description = """
            [모든 Role 가능] 팀을 삭제합니다.<br>
            팀이 삭제되면, 팀에 속한 팀리더와 팀원들의 소속이 사라집니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{teamId}")
    public ApiResponse<Long> deleteTeam(
        @PathVariable(name = "teamId") Long teamId
    ) {
        Long id = teamService.delete(teamId);
        return ApiResponse.success(id);
    }
}
