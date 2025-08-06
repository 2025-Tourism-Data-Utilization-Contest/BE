package com.saerok.showing.api.domain.post.service;

import com.saerok.showing.api.domain.comment.service.CommentCountProvider;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.dto.request.PostCreateRequest;
import com.saerok.showing.api.domain.post.dto.request.PostUpdateRequest;
import com.saerok.showing.api.domain.post.dto.response.PostDetailResponse;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.domain.post.repository.PostRepository;
import com.saerok.showing.api.domain.post.service.pagination.PostPaginationStrategy;
import com.saerok.showing.api.domain.post.service.pagination.PostPaginationStrategyFactory;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.file.entity.UploadedFile;
import com.saerok.showing.api.global.file.service.FileService;
import com.saerok.showing.api.global.pagination.cursorResult.CursorResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LoginMemberProvider loginMemberProvider;
    private final FileService fileService;
    private final PostPaginationStrategyFactory postPaginationStrategyFactory;
    private final CommentCountProvider commentCountProvider;

    @Transactional
    public Long save(PostCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        List<UploadedFile> files = fileService.getUploadedFilesByUrls(request.getImageUrls());
        Post post = Post.toEntity(member, request, files);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetails(Long postId) {
        Post post = findById(postId);
        int commentCount = commentCountProvider.getCount(postId);
        return PostDetailResponse.toDto(post, commentCount);
    }

    @Transactional(readOnly = true)
    public CursorResult<PostSummaryResponse> getPosts(
        PostType postType,
        PostSortType sortType,
        String cursorRaw,
        int limit
    ) {
        PostPaginationStrategy strategy = postPaginationStrategyFactory.getStrategy(sortType);
        return strategy.getCursorResult(postType, cursorRaw, limit, commentCountProvider);
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

    public int getPostCount() {
        Long memberId = loginMemberProvider.getCurrentLoginMemberId();
        return postRepository.countByMemberId(memberId);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.POST_NOT_FOUND));
    }
}
