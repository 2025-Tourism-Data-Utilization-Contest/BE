package com.saerok.showing.api.global.pagination.cursorResult;

import com.saerok.showing.api.global.pagination.cursor.CreatedAtCursor;
import com.saerok.showing.api.global.pagination.provider.CreatedAtProvider;
import java.util.List;

public record CreatedAtCursorResult<T extends CreatedAtProvider>(
    List<T> values,
    boolean hasPrevious,
    boolean hasNext
) implements CursorResult<T> {

    public static <T extends CreatedAtProvider> CreatedAtCursorResult<T> of(
        List<T> values,
        CreatedAtCursor cursor,
        int limit
    ) {
        boolean hasPrevious = checkFirstPageByCreatedAt(cursor, values);
        boolean hasNext = values.size() > limit;
        List<T> trimmedValues = hasNext ? values.subList(0, limit) : values;
        return new CreatedAtCursorResult<>(trimmedValues, hasPrevious, hasNext);
    }

    private static <T extends CreatedAtProvider> boolean checkFirstPageByCreatedAt(
        CreatedAtCursor cursor,
        List<T> values
    ) {
        if (cursor == null || values.isEmpty()) {
            return false;
        }
        return values.getFirst().getCreatedAt().isAfter(cursor.createdAt());
    }
}
