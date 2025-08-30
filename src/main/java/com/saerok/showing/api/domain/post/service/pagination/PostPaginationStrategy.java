package com.saerok.showing.api.domain.post.service.pagination;

import com.saerok.showing.api.domain.comment.service.CommentCountProvider;
import com.saerok.showing.api.domain.like.service.LikeReadService;
import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.post.dto.response.PostSummaryResponse;
import com.saerok.showing.api.domain.post.entity.Post;
import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.domain.post.entity.PostType;
import com.saerok.showing.api.global.pagination.cursorResult.CursorResult;
import java.util.List;

public interface PostPaginationStrategy {

    PostSortType getSortType();

    Object parseCursor(String rawCursor);

    List<Post> paginate(Long teamId, PostType postType, Object cursor, int limit);

    CursorResult<PostSummaryResponse> getCursorResult(
        Long teamId,
        PostType postType,
        String rawCursor,
        int limit,
        CommentCountProvider commentCountProvider,
        LikeReadService likeReadService,
        Member currentMember
    );
}
