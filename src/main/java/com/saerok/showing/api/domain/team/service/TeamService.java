package com.saerok.showing.api.domain.team.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.memberTeam.entity.MemberTeam;
import com.saerok.showing.api.domain.memberTeam.service.MemberTeamService;
import com.saerok.showing.api.domain.team.dto.request.TeamCreateRequest;
import com.saerok.showing.api.domain.team.dto.request.TeamJoinRequest;
import com.saerok.showing.api.domain.team.dto.response.MyTeamResponse;
import com.saerok.showing.api.domain.team.dto.response.TeamMemberResponse;
import com.saerok.showing.api.domain.team.entity.Team;
import com.saerok.showing.api.domain.team.repository.TeamRepository;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberTeamService memberTeamService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long save(TeamCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        validateUniqueTeamName(request.getName());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Team team = Team.toEntity(request, member, encodedPassword);
        teamRepository.save(team);
        memberTeamService.addMemberToTeam(member, team);
        return team.getId();
    }

    @Transactional
    public Long joinTeam(TeamJoinRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Team team = findByName(request.getName());
        validateTeamPassword(team, request.getPassword());
        memberTeamService.addMemberToTeam(member, team);
        return team.getId();
    }

    @Transactional
    public Long leaveTeam(Long teamId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Team team = findById(teamId);
        if (team.isLeader(member)) {
            deleteTeamAsLeader(team);
        } else {
            memberTeamService.removeMemberFromTeam(member, team);
        }
        return teamId;
    }

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long teamId) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        validateIsMemberOfTeam(currentMember, teamId);
        return memberTeamService.getMembersByTeamId(teamId).stream()
            .map(MemberTeam::getMember)
            .sorted(Comparator.comparing(Member::getCreatedAt))
            .map(member -> TeamMemberResponse.toDto(member, teamId))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<MyTeamResponse> getMyTeams() {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        return memberTeamService.getTeamsByMemberId(currentMember.getId()).stream()
            .map(MemberTeam::getTeam) // MemberTeam → Team
            .map(team -> MyTeamResponse.toDto(team, currentMember))
            .toList();
    }

    @Transactional
    public Long delete(Long teamId) {
        Team team = findById(teamId);
        assertLeader(team);
        List<MemberTeam> members = memberTeamService.getMembersByTeamId(teamId);
        for (MemberTeam mt : members) {
            memberTeamService.removeMemberFromTeam(mt.getMember(), team);
        }
        teamRepository.deleteById(teamId);
        return teamId;
    }

    private Team findById(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.TEAM_NOT_FOUND));
    }

    private Team findByName(String name) {
        return teamRepository.findByName(name)
            .orElseThrow(() -> ShowingException.from(ErrorCode.TEAM_NOT_FOUND));
    }

    private void validateUniqueTeamName(String name) {
        if (teamRepository.existsByName(name)) {
            throw ShowingException.from(ErrorCode.DUPLICATE_TEAM_NAME);
        }
    }

    private void validateTeamPassword(Team team, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, team.getPassword())) {
            throw ShowingException.from(ErrorCode.INVALID_TEAM_PASSWORD);
        }
    }

    private void validateIsMemberOfTeam(Member member, Long teamId) {
        boolean isInTeam = memberTeamService.getTeamsByMemberId(member.getId()).stream()
            .anyMatch(mt -> mt.getTeam().getId().equals(teamId));
        if (!isInTeam) {
            throw ShowingException.from(ErrorCode.NOT_MEMBER_OF_TEAM);
        }
    }

    private void assertLeader(Team team) {
        Member currentUser = loginMemberProvider.getCurrentLoginMember();
        if (!team.isLeader(currentUser)) {
            throw ShowingException.from(ErrorCode.NO_TEAM_LEADER_PERMISSION);
        }
    }

    private void deleteTeamAsLeader(Team team) {
        List<MemberTeam> members = memberTeamService.getMembersByTeamId(team.getId());
        for (MemberTeam mt : members) {
            memberTeamService.removeMemberFromTeam(mt.getMember(), team);
        }
        teamRepository.delete(team);
    }
}
