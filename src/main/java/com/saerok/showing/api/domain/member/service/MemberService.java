package com.saerok.showing.api.domain.member.service;

import com.saerok.showing.api.domain.member.dto.request.MemberUpdateRequest;
import com.saerok.showing.api.domain.member.dto.response.MyPageResponse;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.member.repository.MemberRepository;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.service.PostService;
import com.saerok.showing.api.domain.route.dto.response.RouteSummaryResponse;
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
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final RouteService routeService;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional(readOnly = true)
    public MyPageResponse getMember() {
        Member member = loginMemberProvider.getCurrentLoginMember();
        int postCount = postService.getPostCount();
        List<RouteSummaryResponse> routes = routeService.getMyRouteSummaries();
        List<PostSummaryResponse> posts = postService.getMyLikesPostSummaries();
        return MyPageResponse.toDto(member, postCount, routes, posts);
    }

    @Transactional
    public Long update(MemberUpdateRequest memberUpdateRequest) {
        Long memberId = loginMemberProvider.getCurrentLoginMemberId();
        Member member = findById(memberId);
        member.update(memberUpdateRequest);
        return member.getId();
    }

    @Transactional
    public Long delete(Long memberId) {
        memberRepository.deleteById(memberId);
        return memberId;
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> ShowingException.from(ErrorCode.MEMBER_NOT_FOUND));
    }
}
