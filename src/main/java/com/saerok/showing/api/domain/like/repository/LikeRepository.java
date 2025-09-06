package com.saerok.showing.api.domain.like.repository;

import com.saerok.showing.api.domain.comment.entity.Comment;
import com.saerok.showing.api.domain.like.entity.LikeTargetType;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.like.entity.Like;
import com.saerok.showing.api.domain.post.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByMemberAndPost(Member member, Post post);

    Optional<Like> findByMemberAndComment(Member member, Comment comment);

    Optional<Like> findByMemberAndTargetIdAndTargetType(Member member, Long targetId, LikeTargetType targetType);
}
