package com.saerok.showing.api.domain.post.repository;

import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 인기순
    List<Post> findByPostTypeOrderByLikesDesc(PostType postType);
    List<Post> findAllOrderByLikesDesc();

    // 최신순
    List<Post> findByPostTypeOrderByCreatedAtDesc(PostType postType);
    List<Post> findAllByOrderByCreatedAtDesc();
}
