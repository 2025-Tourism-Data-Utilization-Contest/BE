package com.saerok.showing.api.domain.post.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.dto.request.PostCreateRequest;
import com.saerok.showing.api.domain.post.dto.request.PostUpdateRequest;
import com.saerok.showing.api.domain.post.dto.response.PostDetailResponse;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.domain.post.repository.PostRepository;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long save(PostCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = Post.toEntity(member, request);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPost(Long postId) {
        Post post = findById(postId);
        return PostDetailResponse.toDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getAllPosts(PostType postType, PostSortType sortType) {
        List<Post> posts = getPostsByTypeAndSort(postType, sortType);
        return posts.stream()
            .map(PostSummaryResponse::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public Long update(Long postId, PostUpdateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = findById(postId);
        post.validateOwner(member);
        post.update(request);
        return post.getId();
    }

    @Transactional
    public Long delete(Long postId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = findById(postId);
        post.validateOwner(member);
        postRepository.delete(post);
        return postId;
    }

    private Post findById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.POST_NOT_FOUND));
    }

    private List<Post> getPostsByTypeAndSort(PostType postType, PostSortType sortType) {
        boolean isPopular = sortType == PostSortType.POPULAR;
        if (postType == null) {
            return isPopular
                ? postRepository.findAllOrderByLikesDesc()
                : postRepository.findAllByOrderByCreatedAtDesc();
        }
        return isPopular
            ? postRepository.findByPostTypeOrderByLikesDesc(postType)
            : postRepository.findByPostTypeOrderByCreatedAtDesc(postType);
    }
}
