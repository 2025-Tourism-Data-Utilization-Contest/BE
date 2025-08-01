package com.saerok.showing.api.domain.team.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.repository.MemberRepository;
import com.saerok.showing.api.domain.team.dto.request.TeamCreateRequest;
import com.saerok.showing.api.domain.team.dto.request.TeamJoinRequest;
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
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long save(TeamCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        validateNotAlreadyJoined(member);
        validateUniqueTeamName(request.getName());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Team team = Team.toEntity(request, member, encodedPassword);
        teamRepository.save(team);
        team.addMember(member);
        memberRepository.save(member);
        return team.getId();
    }

    @Transactional
    public Long joinTeam(TeamJoinRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        validateNotAlreadyJoined(member);
        Team team = findByName(request.getName());
        validateTeamPassword(team, request.getPassword());
        member.joinTeam(team);
        memberRepository.save(member);
        return team.getId();
    }

    @Transactional
    public Long leaveTeam(Long teamId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Team team = findById(teamId);
        validateIsMemberOfTeam(member, team);
        if (team.isLeader(member)) {
            deleteTeamAsLeader(team);
        } else {
            leaveTeamAsMember(member);
        }
        return teamId;
    }

    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getMyTeamMembers() {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Team team = getTeamOfCurrentMember();
        List<Member> members = team.getMembers();
        return members.stream()
            .sorted(Comparator.comparing(Member::getCreatedAt))
            .map(TeamMemberResponse::toDto)
            .toList();
    }

    @Transactional
    public Long delete(Long teamId) {
        Team team = findById(teamId);
        assertLeader(team);
        List<Member> members = team.getMembers();
        removeAllMembersFromTeam(team);
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

    private Team getTeamOfCurrentMember() {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        if (currentMember.getTeam() == null) {
            throw ShowingException.from(ErrorCode.NOT_MEMBER_OF_TEAM);
        }
        return teamRepository.findByIdWithMembers(currentMember.getTeam().getId())
            .orElseThrow(() -> ShowingException.from(ErrorCode.TEAM_NOT_FOUND));
    }

    private void validateUniqueTeamName(String name) {
        if (teamRepository.existsByName(name)) {
            throw ShowingException.from(ErrorCode.DUPLICATE_TEAM_NAME);
        }
    }

    private void validateNotAlreadyJoined(Member member) {
        if (member.getTeam() != null) {
            throw ShowingException.from(ErrorCode.ALREADY_JOINED_TEAM);
        }
    }

    private void validateTeamPassword(Team team, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, team.getPassword())) {
            throw ShowingException.from(ErrorCode.INVALID_TEAM_PASSWORD);
        }
    }

    private void validateIsMemberOfTeam(Member member, Team team) {
        if (member.getTeam() == null || !team.getId().equals(member.getTeam().getId())) {
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
        for (Member m : team.getMembers()) {
            m.leaveTeam();
            memberRepository.save(m);
        }
        teamRepository.delete(team);
    }

    private void leaveTeamAsMember(Member member) {
        member.leaveTeam();
        memberRepository.save(member);
    }

    private void removeAllMembersFromTeam(Team team) {
        team.getMembers().forEach(member -> member.setTeam(null));
    }
}
