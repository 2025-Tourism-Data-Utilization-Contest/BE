package com.saerok.showing.api.global.pagination.cursorResult;

import com.saerok.showing.api.global.pagination.provider.IdProvider;
import java.util.List;
import lombok.Getter;

@Getter
public record IdCursorResult<T extends IdProvider>(
    List<T> values,
    boolean hasPrevious,
    boolean hasNext
) {
    public static <T extends IdProvider> IdCursorResult<T> of(List<T> values, Long cursor, int limit) {
        boolean hasPrevious = checkFirstPageById(cursor, values);

        boolean hasNext = values.size() > limit;
        List<T> trimmedValues = hasNext ? values.subList(0, limit) : values;

        return new IdCursorResult<>(trimmedValues, hasPrevious, hasNext);
    }

    private static <T extends IdProvider> boolean checkFirstPageById(Long cursor, List<T> values) {
        return cursor != null
            && cursor > 0
            && !values.isEmpty()
            && values.getFirst().getId() > cursor;
    }
}
