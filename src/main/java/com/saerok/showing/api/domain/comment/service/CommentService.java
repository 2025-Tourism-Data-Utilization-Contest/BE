package com.saerok.showing.api.domain.comment.service;

import com.saerok.showing.api.domain.like.service.LikeReadService;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.comment.dto.request.CommentCreateRequest;
import com.saerok.showing.api.domain.comment.dto.response.CommentResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.comment.entity.Comment;
import com.saerok.showing.api.domain.comment.repository.CommentRepository;
import com.saerok.showing.api.domain.post.service.PostService;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final LoginMemberProvider loginMemberProvider;
    private final PostService postService;
    private final LikeReadService likeReadService;

    @Transactional
    public Long create(Long postId, CommentCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = postService.findById(postId);
        Comment comment = Comment.toEntity(member, post, request);
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream()
            .map(comment -> {
                boolean isLiked = likeReadService.isCommentLiked(currentMember, comment.getId());
                return CommentResponse.toDto(comment, isLiked);
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long postId, Long commentId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Comment comment = findCommentByIdAndPostId(commentId, postId);
        comment.validateOwner(member);
        commentRepository.delete(comment);
        return commentId;
    }

    private Comment findCommentByIdAndPostId(Long commentId, Long postId) {
        return commentRepository.findByIdAndPostId(commentId, postId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.COMMENT_NOT_FOUND));
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.COMMENT_NOT_FOUND));
    }
}
