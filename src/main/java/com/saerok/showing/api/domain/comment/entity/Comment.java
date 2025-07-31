package com.saerok.showing.api.domain.comment.entity;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.comment.dto.request.CommentCreateRequest;
import com.saerok.showing.api.domain.poll.entity.Poll;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.global.entity.BaseEntity;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @Column(name = "comment", nullable = false, length = 1000)
    private String comment;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    public static Comment toEntity(Member member, Post post, CommentCreateRequest request) {
        return Comment.builder()
            .member(member)
            .post(post)
            .comment(request.getComment())
            .likeCount(0)
            .build();
    }

    public void validateOwner(Member loginMember) {
        if (!this.member.getId().equals(loginMember.getId())) {
            throw ShowingException.from(ErrorCode.COMMENT_WRITER_MISMATCH);
        }
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
