package com.saerok.showing.api.domain.comment.dto.response;

import com.saerok.showing.api.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private Long id;

    private Long postId;

    private String writer;

    private String writerProfileImage;

    private String comment;

    private int likeCount;

    public static CommentResponse toDto(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .postId(comment.getPost().getId())
            .writer(comment.getPost().getMember().getName())
            .writerProfileImage(comment.getPost().getMember().getProfileImage())
            .comment(comment.getComment())
            .likeCount(comment.getLikeCount())
            .build();
    }
}
