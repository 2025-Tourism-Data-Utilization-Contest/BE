package com.saerok.showing.api.domain.like.entity;

import com.saerok.showing.api.domain.comment.entity.Comment;
import com.saerok.showing.api.domain.like.dto.request.LikeToggleRequest;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private LikeTargetType targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public static Like toEntity(Member member, LikeToggleRequest request) {
        return Like.builder()
            .member(member)
            .targetId(request.getTargetId())
            .targetType(request.getLikeTargetType())
            .build();
    }

    public static Like forPost(Member member, Post post) {
        Like like = new Like();
        like.setMember(member);
        like.setPost(post);
        post.getLikes().add(like);
        return like;
    }

    public static Like forComment(Member member, Comment comment) {
        Like like = new Like();
        like.setMember(member);
        like.setComment(comment);
        comment.getLikes().add(like);
        return like;
    }
}
