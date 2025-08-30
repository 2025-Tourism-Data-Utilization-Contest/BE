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

    // --- 전체 게시글 ---

    // 최신순 - 최초 페이지
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'VISIBLE_ALL'
          AND (:postType IS NULL OR p.postType = :postType)
        ORDER BY p.createdAt DESC
        """)
    List<Post> findPublicPostsOrderByCreatedAtDesc(@Param("postType") PostType postType);

    // 최신순 - 커서 페이징
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'VISIBLE_ALL'
          AND (:postType IS NULL OR p.postType = :postType)
          AND p.createdAt < :cursor
        ORDER BY p.createdAt DESC
        """)
    List<Post> findPublicPostsByCreatedAtBefore(
        @Param("postType") PostType postType,
        @Param("cursor") LocalDateTime cursor
    );

    // 인기순 - 최초 페이지
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'VISIBLE_ALL'
          AND (:postType IS NULL OR p.postType = :postType)
        ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findPublicPostsOrderByLikeCountDesc(@Param("postType") PostType postType);

    // 인기순 - 커서 페이징
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'VISIBLE_ALL'
          AND (:postType IS NULL OR p.postType = :postType)
          AND (
              p.likeCount < :likeCount
              OR (p.likeCount = :likeCount AND p.createdAt < :createdAt)
          )
        ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findPublicPostsByPopularCursor(
        @Param("postType") PostType postType,
        @Param("likeCount") int likeCount,
        @Param("createdAt") LocalDateTime createdAt
    );

    // --- 팀별 게시글 ---

    // 최신순 - 최초 페이지
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'TEAM_ONLY'
          AND p.team.id = :teamId
          AND (:postType IS NULL OR p.postType = :postType)
        ORDER BY p.createdAt DESC
        """)
    List<Post> findTeamPostsOrderByCreatedAtDesc(
        @Param("teamId") Long teamId,
        @Param("postType") PostType postType
    );

    // 최신순 - 커서 페이징
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'TEAM_ONLY'
          AND p.team.id = :teamId
          AND (:postType IS NULL OR p.postType = :postType)
          AND p.createdAt < :cursor
        ORDER BY p.createdAt DESC
        """)
    List<Post> findTeamPostsByCreatedAtBefore(
        @Param("teamId") Long teamId,
        @Param("postType") PostType postType,
        @Param("cursor") LocalDateTime cursor
    );

    // 인기순 - 최초 페이지
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'TEAM_ONLY'
          AND p.team.id = :teamId
          AND (:postType IS NULL OR p.postType = :postType)
        ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findTeamPostsOrderByLikeCountDesc(
        @Param("teamId") Long teamId,
        @Param("postType") PostType postType
    );

    // 인기순 - 커서 페이징
    @Query("""
        SELECT p FROM Post p
        WHERE p.visibility = 'TEAM_ONLY'
          AND p.team.id = :teamId
          AND (:postType IS NULL OR p.postType = :postType)
          AND (
              p.likeCount < :likeCount
              OR (p.likeCount = :likeCount AND p.createdAt < :createdAt)
          )
        ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findTeamPostsByPopularCursor(
        @Param("teamId") Long teamId,
        @Param("postType") PostType postType,
        @Param("likeCount") int likeCount,
        @Param("createdAt") LocalDateTime createdAt
    );

    int countByMemberId(Long memberId);
}
