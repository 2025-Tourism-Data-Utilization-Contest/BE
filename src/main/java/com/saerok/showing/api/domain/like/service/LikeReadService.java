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
}
