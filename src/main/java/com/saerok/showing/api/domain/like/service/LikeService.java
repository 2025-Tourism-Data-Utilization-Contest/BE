package com.saerok.showing.api.domain.like.service;

import com.saerok.showing.api.domain.comment.entity.Comment;
import com.saerok.showing.api.domain.comment.service.CommentService;
import com.saerok.showing.api.domain.like.dto.request.LikeToggleRequest;
import com.saerok.showing.api.domain.like.entity.LikeTargetType;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.like.entity.Like;
import com.saerok.showing.api.domain.like.repository.LikeRepository;
import com.saerok.showing.api.domain.post.service.PostService;
import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.route.service.RouteService;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final LoginMemberProvider loginMemberProvider;
    private final PostService postService;
    private final CommentService commentService;
    private final RouteService routeService;

    @Transactional
    public boolean toggleLike(LikeToggleRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Long targetId = request.getTargetId();
        LikeTargetType type = request.getLikeTargetType();
        switch (type) {
            case POST -> {
                Post post = postService.findById(targetId);
                return handleToggle(member, request, post);
            }
            case COMMENT -> {
                Comment comment = commentService.findById(targetId);
                return handleToggle(member, request, comment);
            }
            case ROUTE -> {
                Route route = routeService.findById(targetId);
                return handleToggle(member, request, route);
            }
            default -> throw ShowingException.from(ErrorCode.INVALID_LIKE_TARGET_TYPE);
        }
    }

    private boolean handleToggle(Member member, LikeToggleRequest request, Post post) {
        Optional<Like> existingLike = likeRepository.findByMemberAndTargetIdAndTargetType(
            member, request.getTargetId(), LikeTargetType.POST
        );
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            post.decreaseLikeCount();
            return false;
        }
        likeRepository.save(Like.toEntity(member, request));
        post.increaseLikeCount();
        return true;
    }

    private boolean handleToggle(Member member, LikeToggleRequest request, Comment comment) {
        Optional<Like> existingLike = likeRepository.findByMemberAndTargetIdAndTargetType(
            member, request.getTargetId(), LikeTargetType.COMMENT
        );
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            comment.decreaseLikeCount();
            return false;
        }
        likeRepository.save(Like.toEntity(member, request));
        comment.increaseLikeCount();
        return true;
    }

    private boolean handleToggle(Member member, LikeToggleRequest request, Route route) {
        Optional<Like> existingLike = likeRepository.findByMemberAndTargetIdAndTargetType(
            member, request.getTargetId(), LikeTargetType.ROUTE
        );
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            route.decreaseLikeCount();
            return false;
        }
        likeRepository.save(Like.toEntity(member, request));
        route.increaseLikeCount();
        return true;
    }
}
