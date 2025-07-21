package com.saerok.showing.api.domain.post.repository;

import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    int countCommentsOfPost(@Param("postId") Long postId);

    // 인기순
    List<Post> findByPostTypeOrderByLikeCountDesc(PostType postType);
    List<Post> findAllByOrderByLikeCountDesc();

    // 최신순
    List<Post> findByPostTypeOrderByCreatedAtDesc(PostType postType);
    List<Post> findAllByOrderByCreatedAtDesc();
}
