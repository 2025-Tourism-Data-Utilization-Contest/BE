package com.saerok.showing.api.domain.review.entity;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.review.dto.request.ReviewCreateRequest;
import com.saerok.showing.api.domain.review.dto.request.ReviewUpdateRequest;
import com.saerok.showing.api.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 50)
    private ReviewTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", nullable = false)
    private String comment;

    public static Review toEntity(Member member, ReviewCreateRequest request) {
        return Review.builder()
            .member(member)
            .targetId(request.getTargetId())
            .targetType(request.getTargetType())
            .rating(request.getRating())
            .comment(request.getComment())
            .build();
    }

    public void update(ReviewUpdateRequest request) {
        this.rating = request.getRating();
        this.comment = request.getComment();
    }
}
