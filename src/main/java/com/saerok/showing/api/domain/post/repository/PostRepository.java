package com.saerok.showing.api.domain.post.repository;

import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 1. 커서가 없을 때 (최초 요청) - 최신순
    @Query("""
            SELECT p FROM Post p
            WHERE (:postType IS NULL OR p.postType = :postType)
            ORDER BY p.createdAt DESC
        """)
    List<Post> findByPostTypeOrderByCreatedAtDesc(
        @Param("postType") PostType postType
    );

    // 2. 커서가 있을 때 (이후 페이지 요청) - 최신순
    @Query("""
            SELECT p FROM Post p
            WHERE (:postType IS NULL OR p.postType = :postType)
              AND p.createdAt < :cursor
            ORDER BY p.createdAt DESC
        """)
    List<Post> findByPostTypeAndCreatedAtBeforeOrderByCreatedAtDesc(
        @Param("postType") PostType postType,
        @Param("cursor") LocalDateTime cursor
    );

    // 1. 커서가 없을 때 (인기순 기본 정렬) - 인기순
    @Query("""
            SELECT p FROM Post p
            WHERE (:postType IS NULL OR p.postType = :postType)
            ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findByPostTypeOrderByLikeCountDesc(
        @Param("postType") PostType postType
    );

    // 2. 커서가 있을 때 (likeCount, createdAt 기준 페이징) - 인기순
    @Query("""
            SELECT p FROM Post p
            WHERE (:postType IS NULL OR p.postType = :postType)
              AND (
                p.likeCount < :likeCount
                OR (p.likeCount = :likeCount AND p.createdAt < :createdAt)
              )
            ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findByPopularCursor(
        @Param("postType") PostType postType,
        @Param("likeCount") int likeCount,
        @Param("createdAt") LocalDateTime createdAt
    );

    int countByMemberId(Long memberId);
}
