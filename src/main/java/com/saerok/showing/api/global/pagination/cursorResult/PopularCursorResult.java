package com.saerok.showing.api.global.pagination.cursorResult;

import com.saerok.showing.api.global.pagination.cursor.PopularCursor;
import com.saerok.showing.api.global.pagination.provider.PopularProvider;
import java.util.List;

public record PopularCursorResult<T extends PopularProvider>(
    List<T> values,
    boolean hasPrevious,
    boolean hasNext
) implements CursorResult<T> {

    public static <T extends PopularProvider> PopularCursorResult<T> of(
        List<T> values, PopularCursor cursor, int limit
    ) {
        boolean hasPrevious = checkFirstPageByPopularity(cursor, values);
        boolean hasNext = values.size() > limit;
        List<T> trimmedValues = hasNext ? values.subList(0, limit) : values;

        return new PopularCursorResult<>(trimmedValues, hasPrevious, hasNext);
    }

    private static <T extends PopularProvider> boolean checkFirstPageByPopularity(
        PopularCursor cursor, List<T> values
    ) {
        if (cursor == null || values == null || values.isEmpty()) {
            return false;
        }
        T first = values.getFirst();
        return first.getLikeCount() > cursor.likeCount()
            || (first.getLikeCount() == cursor.likeCount() && first.getCreatedAt().isAfter(cursor.createdAt()));
    }
}
