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
import com.saerok.showing.api.global.pagination.cursor.CreatedAtCursor;
import com.saerok.showing.api.global.pagination.cursorResult.CreatedAtCursorResult;
import com.saerok.showing.api.global.pagination.cursorResult.CursorResult;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LatestPostPaginationStrategy implements PostPaginationStrategy {

    private final PostRepository postRepository;

    @Override
    public PostSortType getSortType() {
        return PostSortType.LATEST;
    }

    @Override
    public Object parseCursor(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return new CreatedAtCursor(LocalDateTime.parse(raw));
        } catch (Exception e) {
            throw ShowingException.from(ErrorCode.INVALID_CURSOR_FORMAT);
        }
    }

    @Override
    public List<Post> paginate(Long teamId, PostType postType, Object cursor, int limit) {
        LocalDateTime cursorTime = (cursor instanceof CreatedAtCursor c) ? c.createdAt() : null;
        List<Post> posts;
        if (teamId == null) {
            // 전체 피드 (VISIBLE_ALL)
            posts = (cursorTime == null
                ? postRepository.findPublicPostsOrderByCreatedAtDesc(postType)
                : postRepository.findPublicPostsByCreatedAtBefore(postType, cursorTime));
        } else {
            // 팀 피드 (TEAM_ONLY)
            posts = (cursorTime == null
                ? postRepository.findTeamPostsOrderByCreatedAtDesc(teamId, postType)
                : postRepository.findTeamPostsByCreatedAtBefore(teamId, postType, cursorTime));
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
        CreatedAtCursor cursor = (CreatedAtCursor) parseCursor(cursorRaw);
        List<Post> posts = paginate(teamId, postType, cursor, limit);
        List<PostSummaryResponse> responses = posts.stream()
            .map(post -> {
                boolean isLiked = likeReadService.isPostLiked(currentMember, post.getId());
                int commentCount = commentCountProvider.getCount(post.getId());
                return PostSummaryResponse.toDto(post, commentCount, isLiked);
            })
            .toList();
        return CreatedAtCursorResult.of(responses, cursor, limit);
    }
}
