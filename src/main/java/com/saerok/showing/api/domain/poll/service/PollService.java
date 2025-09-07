package com.saerok.showing.api.domain.poll.service;

import com.saerok.showing.api.domain.like.service.LikeReadService;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.memberTeam.service.MemberTeamService;
import com.saerok.showing.api.domain.place.dto.response.PlaceSummaryResponse;
import com.saerok.showing.api.domain.poll.dto.request.PollCandidateRegisterRequest;
import com.saerok.showing.api.domain.poll.dto.request.PollCreateRequest;
import com.saerok.showing.api.domain.poll.dto.request.PollUpdateRequest;
import com.saerok.showing.api.domain.poll.dto.response.PollDetailResponse;
import com.saerok.showing.api.domain.poll.entity.Poll;
import com.saerok.showing.api.domain.poll.repository.PollRepository;
import com.saerok.showing.api.domain.route.dto.response.RouteSummaryResponse;
import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.route.service.RouteService;
import com.saerok.showing.api.domain.team.entity.Team;
import com.saerok.showing.api.domain.team.service.TeamService;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PollRepository pollRepository;
    private final TeamService teamService;
    private final LikeReadService likeReadService;
    private final RouteService routeService;
    private final MemberTeamService memberTeamService;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long save(PollCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Team team = teamService.findById(request.getTeamId());
        Poll poll = Poll.toEntity(request, member, team);
        validateTeamMember(poll, member);
        pollRepository.save(poll);
        return poll.getId();
    }

    @Transactional(readOnly = true)
    public List<PollDetailResponse> getTeamPolls(Long teamId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        validateTeamMember(teamId, member.getId());
        return pollRepository.findAllByTeamId(teamId)
            .stream()
            .map(poll -> {
                int commentCount = pollRepository.countCommentsOfPoll(poll.getId());
                List<RouteSummaryResponse> routeSummaries = poll.getRouteOptions().stream()
                    .map(route -> {
                        boolean isPollOptionLiked = likeReadService.isPollOptionLiked(member, route.getId());
                        return RouteSummaryResponse.toDto(route, List.of(), isPollOptionLiked);
                    })
                    .toList();
                boolean isLiked = likeReadService.isPollLiked(member, poll.getId());
                return PollDetailResponse.toDto(poll, poll.getMember(), commentCount, routeSummaries, isLiked);
            })
            .toList();
    }

    @Transactional(readOnly = true)
    public PollDetailResponse getPoll(Long pollId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        int commentCount = getCommentCount(pollId);
        List<RouteSummaryResponse> routeSummaries = poll.getRouteOptions().stream()
            .map(route -> {
                boolean isPollOptionLiked = likeReadService.isPollOptionLiked(member, route.getId());
                List<PlaceSummaryResponse> placeSummaries = route.getRoutePlaces().stream()
                    .map(routePlace -> PlaceSummaryResponse.create(routePlace.getPlaceName()))
                    .toList();
                return RouteSummaryResponse.toDto(route, placeSummaries, isPollOptionLiked);
            })
            .toList();
        boolean isLiked = likeReadService.isPollLiked(member, pollId);
        return PollDetailResponse.toDto(poll, poll.getMember(), commentCount, routeSummaries, isLiked);
    }

    @Transactional
    public void registerCandidate(Long pollId, PollCandidateRegisterRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        validateTeamMember(poll, member);
        Route route = routeService.findById(request.getRouteId());
        poll.registerRouteOption(route);
    }

    @Transactional
    public Long update(Long pollId, PollUpdateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        validateTeamMember(poll, member);
        poll.validateOwner(poll, member);
        poll.update(request);
        return poll.getId();
    }

    @Transactional
    public Long delete(Long pollId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        validateTeamMember(poll, member);
        poll.validateOwner(poll, member);
        poll.getRouteOptions().forEach(route -> route.setPoll(null));
        pollRepository.delete(poll);
        return pollId;
    }

    private void validateTeamMember(Long teamId, Long memberId) {
        if (!memberTeamService.existsByMemberIdAndTeamId(memberId, teamId)) {
            throw ShowingException.from(ErrorCode.NOT_MEMBER_OF_TEAM);
        }
    }


    private void validateTeamMember(Poll poll, Member member) {
        if (!memberTeamService.existsByMemberIdAndTeamId(member.getId(), poll.getTeam().getId())) {
            throw ShowingException.from(ErrorCode.NOT_MEMBER_OF_TEAM);
        }
    }

    private int getCommentCount(Long pollId) {
        return pollRepository.countCommentsOfPoll(pollId);
    }

    public Poll findById(Long pollId) {
        return pollRepository.findById(pollId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.POLL_NOT_FOUND));
    }
}
