package com.saerok.showing.api.global.pagination.cursorResult;

import com.saerok.showing.api.global.pagination.cursor.IdCursor;
import com.saerok.showing.api.global.pagination.provider.IdProvider;

import java.util.List;

public record IdCursorResult<T extends IdProvider>(
    List<T> values,
    boolean hasPrevious,
    boolean hasNext
) implements CursorResult<T> {

    public static <T extends IdProvider> IdCursorResult<T> of(
        List<T> values,
        IdCursor cursor,
        int limit
    ) {
        boolean hasPrevious = checkFirstPageById(cursor, values);
        boolean hasNext = values.size() > limit;
        List<T> trimmedValues = hasNext ? values.subList(0, limit) : values;
        return new IdCursorResult<>(trimmedValues, hasPrevious, hasNext);
    }

    private static <T extends IdProvider> boolean checkFirstPageById(
        IdCursor cursor,
        List<T> values
    ) {
        if (cursor == null || values.isEmpty()) {
            return false;
        }
        return values.getFirst().getId() > cursor.id();
    }
}
