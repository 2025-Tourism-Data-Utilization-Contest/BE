package com.saerok.showing.api.domain.post.service.pagination;

import com.saerok.showing.api.domain.comment.service.CommentCountProvider;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.global.pagination.cursorResult.CursorResult;
import java.util.List;

public interface PostPaginationStrategy {

    PostSortType getSortType();

    Object parseCursor(String rawCursor);

    List<Post> paginate(PostType postType, Object cursor, int limit, List<Long> teamIds);

    CursorResult<PostSummaryResponse> getCursorResult(
        PostType postType,
        String rawCursor,
        int limit,
        List<Long> teamIds,
        CommentCountProvider commentCountProvider
    );
}
