package com.saerok.showing.api.domain.post.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostLike;
import com.saerok.showing.api.domain.post.repository.PostLikeRepository;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final LoginMemberProvider loginMemberProvider;


    @Transactional
    public boolean toggleLike(Long postId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.POST_NOT_FOUND));

        return postLikeRepository.findByMemberAndPost(member, post)
            .map(like -> {
                postLikeRepository.delete(like);
                return false; // 좋아요 취소
            })
            .orElseGet(() -> {
                PostLike newLike = PostLike.of(member, post);
                postLikeRepository.save(newLike);
                return true; // 좋아요 추가
            });
    }
}
