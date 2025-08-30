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
    public List<Post> paginate(PostType postType, Object cursor, int limit, List<Long> teamIds) {
        if (cursor == null) {
            return postRepository.findVisiblePostsOrderByLikeCountDesc(postType, teamIds)
                .stream()
                .limit(limit + 1)
                .toList();
        }
        if (cursor instanceof PopularCursor c) {
            return postRepository.findVisiblePostsByPopularCursor(postType, c.likeCount(), c.createdAt(), teamIds)
                .stream()
                .limit(limit + 1)
                .toList();
        }
        throw ShowingException.from(ErrorCode.INVALID_CURSOR_FORMAT);
    }

    @Override
    public CursorResult<PostSummaryResponse> getCursorResult(
        PostType postType,
        String cursorRaw,
        int limit,
        List<Long> teamIds,
        CommentCountProvider commentCountProvider,
        LikeReadService likeReadService,
        Member currentMember
    ) {
        PopularCursor cursor = (PopularCursor) parseCursor(cursorRaw);
        List<Post> posts = paginate(postType, cursor, limit, teamIds);
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
