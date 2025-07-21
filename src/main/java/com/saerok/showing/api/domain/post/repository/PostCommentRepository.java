package com.saerok.showing.api.domain.post.repository;

import com.saerok.showing.api.domain.post.entity.PostComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    List<PostComment> findAllByPostId(Long postId);

    Optional<PostComment> findByIdAndPostId(Long commentId, Long postId);
}
