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

    // 최신순 - 최초 페이지
    @Query("""
        SELECT DISTINCT p
        FROM Post p
        LEFT JOIN MemberTeam mt ON p.member.id = mt.member.id
        WHERE (:postType IS NULL OR p.postType = :postType)
          AND (
              p.visibility = 'VISIBLE_ALL'
              OR (p.visibility = 'TEAM_ONLY' AND mt.team.id IN :teamIds)
          )
        ORDER BY p.createdAt DESC
        """)
    List<Post> findVisiblePostsOrderByCreatedAtDesc(
        @Param("postType") PostType postType,
        @Param("teamIds") List<Long> teamIds
    );

    // 최신순 - 커서 페이징
    @Query("""
        SELECT DISTINCT p
        FROM Post p
        LEFT JOIN MemberTeam mt ON p.member.id = mt.member.id
        WHERE (:postType IS NULL OR p.postType = :postType)
          AND p.createdAt < :cursor
          AND (
              p.visibility = 'VISIBLE_ALL'
              OR (p.visibility = 'TEAM_ONLY' AND mt.team.id IN :teamIds)
          )
        ORDER BY p.createdAt DESC
        """)
    List<Post> findVisiblePostsByCreatedAtBefore(
        @Param("postType") PostType postType,
        @Param("cursor") LocalDateTime cursor,
        @Param("teamIds") List<Long> teamIds
    );

    // 인기순 - 최초 페이지
    @Query("""
        SELECT DISTINCT p
        FROM Post p
        LEFT JOIN MemberTeam mt ON p.member.id = mt.member.id
        WHERE (:postType IS NULL OR p.postType = :postType)
          AND (
              p.visibility = 'VISIBLE_ALL'
              OR (p.visibility = 'TEAM_ONLY' AND mt.team.id IN :teamIds)
          )
        ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findVisiblePostsOrderByLikeCountDesc(
        @Param("postType") PostType postType,
        @Param("teamIds") List<Long> teamIds
    );

    // 인기순 - 커서 페이징
    @Query("""
        SELECT DISTINCT p
        FROM Post p
        LEFT JOIN MemberTeam mt ON p.member.id = mt.member.id
        WHERE (:postType IS NULL OR p.postType = :postType)
          AND (
              p.likeCount < :likeCount
              OR (p.likeCount = :likeCount AND p.createdAt < :createdAt)
          )
          AND (
              p.visibility = 'VISIBLE_ALL'
              OR (p.visibility = 'TEAM_ONLY' AND mt.team.id IN :teamIds)
          )
        ORDER BY p.likeCount DESC, p.createdAt DESC
        """)
    List<Post> findVisiblePostsByPopularCursor(
        @Param("postType") PostType postType,
        @Param("likeCount") int likeCount,
        @Param("createdAt") LocalDateTime createdAt,
        @Param("teamIds") List<Long> teamIds
    );

    int countByMemberId(Long memberId);
}
