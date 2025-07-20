package com.saerok.showing.api.domain.post.service;

import com.saerok.showing.api.domain.post.repository.PostCommentRepository;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final LoginMemberProvider loginMemberProvider;


}
