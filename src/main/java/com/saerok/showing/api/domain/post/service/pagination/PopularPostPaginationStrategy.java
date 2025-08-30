package com.saerok.showing.api.domain.post.service.pagination;

import com.saerok.showing.api.domain.comment.service.CommentCountProvider;
import com.saerok.showing.api.domain.like.service.LikeReadService;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.domain.post.repository.PostRepository;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.pagination.cursor.PopularCursor;
import com.saerok.showing.api.global.pagination.cursorResult.CursorResult;
import com.saerok.showing.api.global.pagination.cursorResult.PopularCursorResult;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopularPostPaginationStrategy implements PostPaginationStrategy {

    private final PostRepository postRepository;

    @Override
    public PostSortType getSortType() {
        return PostSortType.POPULAR;
    }

    @Override
    public Object parseCursor(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            String[] parts = raw.split("\\|");
            int likeCount = Integer.parseInt(parts[0]);
            LocalDateTime createdAt = LocalDateTime.parse(parts[1]);
            return new PopularCursor(likeCount, createdAt);
        } catch (Exception e) {
            throw ShowingException.from(ErrorCode.INVALID_CURSOR_FORMAT);
        }
    }

    @Override
    public List<Post> paginate(Long teamId, PostType postType, Object cursor, int limit) {
        PopularCursor c = (cursor instanceof PopularCursor pc) ? pc : null;
        List<Post> posts;
        if (teamId == null) {
            // 전체 피드 (VISIBLE_ALL)
            posts = (c == null
                ? postRepository.findPublicPostsOrderByLikeCountDesc(postType)
                : postRepository.findPublicPostsByPopularCursor(postType, c.likeCount(), c.createdAt()));
        } else {
            // 팀 피드 (TEAM_ONLY)
            posts = (c == null
                ? postRepository.findTeamPostsOrderByLikeCountDesc(teamId, postType)
                : postRepository.findTeamPostsByPopularCursor(teamId, postType, c.likeCount(), c.createdAt()));
        }
        return posts.stream()
            .limit(limit + 1)
            .toList();
    }

    @Override
    public CursorResult<PostSummaryResponse> getCursorResult(
        Long teamId,
        PostType postType,
        String cursorRaw,
        int limit,
        CommentCountProvider commentCountProvider,
        LikeReadService likeReadService,
        Member currentMember
    ) {
        PopularCursor cursor = (PopularCursor) parseCursor(cursorRaw);
        List<Post> posts = paginate(teamId, postType, cursor, limit);
        List<PostSummaryResponse> responses = posts.stream()
            .map(post -> {
                boolean isLiked = likeReadService.isPostLiked(currentMember, post.getId());
                int commentCount = commentCountProvider.getCount(post.getId());
                return PostSummaryResponse.toDto(post, commentCount, isLiked);
            })
            .toList();
        return PopularCursorResult.of(responses, cursor, limit);
    }
}