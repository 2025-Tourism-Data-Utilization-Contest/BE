package com.saerok.showing.api.domain.like.service;

import com.saerok.showing.api.domain.like.entity.LikeTargetType;
import com.saerok.showing.api.domain.like.repository.LikeRepository;
import com.saerok.showing.api.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeReadService {

    private final LikeRepository likeRepository;

    @Transactional(readOnly = true)
    public boolean isPostLiked(Member member, Long postId) {
        return likeRepository.findByMemberAndTargetIdAndTargetType(member, postId, LikeTargetType.POST).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isCommentLiked(Member member, Long commentId) {
        return likeRepository.findByMemberAndTargetIdAndTargetType(member, commentId, LikeTargetType.COMMENT).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isRouteLiked(Member member, Long routeId) {
        return likeRepository.findByMemberAndTargetIdAndTargetType(member, routeId, LikeTargetType.ROUTE).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isPollLiked(Member member, Long pollId) {
        return likeRepository.findByMemberAndTargetIdAndTargetType(member, pollId, LikeTargetType.POLL).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean isPollOptionLiked(Member member, Long pollOptionId) {
        return likeRepository.findByMemberAndTargetIdAndTargetType(member, pollOptionId, LikeTargetType.POLL_OPTION).isPresent();
    }
}
