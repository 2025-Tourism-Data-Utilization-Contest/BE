package com.saerok.showing.api.domain.poll.service;

import com.saerok.showing.api.domain.member.entity.Member;
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
    private final LoginMemberProvider loginMemberProvider;
    private final RouteService routeService;

    @Transactional
    public Long save(PollCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = Poll.toEntity(request, member);
        pollRepository.save(poll);
        return poll.getId();
    }

    @Transactional(readOnly = true)
    public PollDetailResponse getPoll(Long pollId) {
        Poll poll = findById(pollId);
        int commentCount = getCommentCount(pollId);
        List<RouteSummaryResponse> routeSummaries = poll.getRouteOptions().stream()
            .map(route -> {
                List<PlaceSummaryResponse> placeSummaries = route.getRoutePlaces().stream()
                    .map(routePlace -> PlaceSummaryResponse.create(routePlace.getPlaceName()))
                    .toList();
                return RouteSummaryResponse.toDto(route, placeSummaries);
            })
            .toList();
        return PollDetailResponse.toDto(poll, poll.getMember(), commentCount, routeSummaries);
    }

    @Transactional
    public void registerCandidate(Long pollId, PollCandidateRegisterRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        Route route = routeService.findById(request.getRouteId());
        poll.registerRouteOption(route);
    }

    @Transactional
    public Long update(Long pollId, PollUpdateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        poll.validateOwner(poll, member);
        poll.update(request);
        return poll.getId();
    }

    @Transactional
    public Long delete(Long pollId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Poll poll = findById(pollId);
        poll.validateOwner(poll, member);
        pollRepository.delete(poll);
        return pollId;
    }

    private int getCommentCount(Long pollId) {
        return pollRepository.countCommentsOfPoll(pollId);
    }

    public Poll findById(Long pollId) {
        return pollRepository.findById(pollId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.POLL_NOT_FOUND));
    }
}
