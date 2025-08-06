package com.saerok.showing.api.domain.comment.service;

import com.saerok.showing.api.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentCountProviderImpl implements CommentCountProvider {

    private final CommentRepository commentRepository;

    @Override
    public int getCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
