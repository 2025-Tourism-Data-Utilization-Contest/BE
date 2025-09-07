package com.saerok.showing.api.domain.post.service;

import com.saerok.showing.api.domain.comment.service.CommentCountProvider;
import com.saerok.showing.api.domain.like.service.LikeReadService;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.memberTeam.service.MemberTeamService;
import com.saerok.showing.api.domain.post.dto.request.PostCreateRequest;
import com.saerok.showing.api.domain.post.dto.request.PostUpdateRequest;
import com.saerok.showing.api.domain.post.dto.response.PostDetailResponse;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.domain.post.entity.PostVisibility;
import com.saerok.showing.api.domain.post.repository.PostRepository;
import com.saerok.showing.api.domain.post.service.pagination.PostPaginationStrategy;
import com.saerok.showing.api.domain.post.service.pagination.PostPaginationStrategyFactory;
import com.saerok.showing.api.domain.team.entity.Team;
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
    private final FileService fileService;
    private final LikeReadService likeReadService;
    private final MemberTeamService memberTeamService;
    private final LoginMemberProvider loginMemberProvider;
    private final CommentCountProvider commentCountProvider;
    private final PostPaginationStrategyFactory postPaginationStrategyFactory;

    @Transactional
    public Long save(PostCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        List<UploadedFile> files = fileService.getUploadedFilesByUrls(request.getImageUrls());
        Team team = resolveTeamIfNeeded(request, member);
        Post post = Post.toEntity(member, request, files, team);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetails(Long postId) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = findById(postId);
        validateReadable(post, member);
        int commentCount = commentCountProvider.getCount(postId);
        boolean isLiked = likeReadService.isPostLiked(member, postId);
        return PostDetailResponse.toDto(post, commentCount, isLiked);
    }

    @Transactional(readOnly = true)
    public CursorResult<PostSummaryResponse> getPosts(
        Long teamId,
        PostType postType,
        PostSortType sortType,
        String cursorRaw,
        int limit
    ) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        PostPaginationStrategy strategy = postPaginationStrategyFactory.getStrategy(sortType);
        if (teamId == null) {
            // 전체 피드 (VISIBLE_ALL만 조회)
            return strategy.getCursorResult(null, postType, cursorRaw, limit, commentCountProvider, likeReadService,
                member
            );
        }
        // 팀 피드 (TEAM_ONLY + 해당 팀만 조회)
        if (!isMemberOfTeam(member.getId(), teamId)) {
            throw ShowingException.from(ErrorCode.NO_TEAM_MEMBER_PERMISSION);
        }
        return strategy.getCursorResult(
            teamId, postType, cursorRaw, limit, commentCountProvider, likeReadService, member
        );
    }

    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getMyLikesPostSummaries() {
        Member member = loginMemberProvider.getCurrentLoginMember();
        return likeReadService.getLikedPosts(member).stream()
            .map(post -> PostSummaryResponse.toDto(
                post,
                post.getComments().size(),
                true
            ))
            .toList();
    }

    @Transactional
    public Long update(Long postId, PostUpdateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Post post = findById(postId);
        post.validateOwner(member);
        Team team = resolveTeamIfNeeded(request, member);
        post.update(request, team);
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

    private Team resolveTeamIfNeeded(PostCreateRequest request, Member member) {
        return resolveTeamIfNeeded(request.getVisibility(), request.getTeamId(), member);
    }

    private Team resolveTeamIfNeeded(PostUpdateRequest request, Member member) {
        return resolveTeamIfNeeded(request.getVisibility(), request.getTeamId(), member);
    }

    // TEAM_ONLY일 경우 teamId 유효성 검증 및 팀 멤버 여부 확인
    private Team resolveTeamIfNeeded(PostVisibility visibility, Long teamId, Member member) {
        if (visibility.isVisibleAll()) {
            return null;
        }
        if (teamId == null) {
            throw ShowingException.from(ErrorCode.INVALID_POST_VISIBILITY);
        }
        Team team = memberTeamService.findTeamById(teamId);
        if (!isMemberOfTeam(member.getId(), team.getId())) {
            throw ShowingException.from(ErrorCode.NO_TEAM_MEMBER_PERMISSION);
        }
        return team;
    }

    // 가시성 검증
    private void validateReadable(Post post, Member member) {
        if (post.getVisibility().isVisibleAll()) {
            return;
        }
        if (!isMemberOfTeam(member.getId(), post.getTeam().getId())) {
            throw ShowingException.from(ErrorCode.NO_TEAM_MEMBER_PERMISSION);
        }
    }

    private boolean isMemberOfTeam(Long memberId, Long teamId) {
        return memberTeamService.existsByMemberIdAndTeamId(memberId, teamId);
    }
}
