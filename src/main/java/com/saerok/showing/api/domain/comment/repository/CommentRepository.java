package com.saerok.showing.api.domain.comment.repository;

import com.saerok.showing.api.domain.comment.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    Optional<Comment> findByIdAndPostId(Long commentId, Long postId);

    int countByPostId(Long postId);
}
