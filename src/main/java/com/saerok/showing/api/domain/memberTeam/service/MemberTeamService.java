package com.saerok.showing.api.domain.memberTeam.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.memberTeam.entity.MemberTeam;
import com.saerok.showing.api.domain.memberTeam.repository.MemberTeamRepository;
import com.saerok.showing.api.domain.team.entity.Team;
import com.saerok.showing.api.domain.team.repository.TeamRepository;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberTeamService {

    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;

    @Transactional
    public void addMemberToTeam(Member member, Team team) {
        validateNotAlreadyJoined(member, team);
        MemberTeam memberTeam = MemberTeam.create(member, team);
        memberTeamRepository.save(memberTeam);
    }

    @Transactional
    public void removeMemberFromTeam(Member member, Team team) {
        MemberTeam memberTeam = findById(member, team);
        memberTeamRepository.delete(memberTeam);
    }

    @Transactional(readOnly = true)
    public List<MemberTeam> getMembersByTeamId(Long teamId) {
        return memberTeamRepository.findWithMemberByTeamId(teamId);
    }

    @Transactional(readOnly = true)
    public List<MemberTeam> getTeamsByMemberId(Long memberId) {
        return memberTeamRepository.findWithTeamByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Team findTeamById(Long teamId) {
        return teamRepository.findById(teamId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.TEAM_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean existsByMemberIdAndTeamId(Long memberId, Long teamId) {
        return memberTeamRepository.existsByMemberIdAndTeamId(memberId, teamId);
    }

    private void validateNotAlreadyJoined(Member member, Team team) {
        if (memberTeamRepository.existsByMemberIdAndTeamId(member.getId(), team.getId())) {
            throw ShowingException.from(ErrorCode.ALREADY_JOINED_TEAM);
        }
    }

    private MemberTeam findById(Member member, Team team) {
        return memberTeamRepository.findByMemberIdAndTeamId(member.getId(), team.getId())
            .orElseThrow(() -> ShowingException.from(ErrorCode.NOT_MEMBER_OF_TEAM));
    }
}
