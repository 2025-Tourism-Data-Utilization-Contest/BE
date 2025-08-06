package com.saerok.showing.api.domain.post.service.pagination;

import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPaginationStrategyFactory {

    private final List<PostPaginationStrategy> strategies;

    public PostPaginationStrategy getStrategy(PostSortType sortType) {
        return strategies.stream()
            .filter(s -> s.getSortType() == sortType)
            .findFirst()
            .orElseThrow(() -> ShowingException.from(ErrorCode.UNSUPPORTED_SORT_TYPE));
    }
}
