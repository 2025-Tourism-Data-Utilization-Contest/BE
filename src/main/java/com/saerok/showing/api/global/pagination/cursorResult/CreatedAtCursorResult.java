package com.saerok.showing.api.global.pagination.cursorResult;

import com.saerok.showing.api.global.pagination.provider.CreatedAtProvider;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public record CreatedAtCursorResult<T extends CreatedAtProvider>(
    List<T> values,
    boolean hasPrevious,
    boolean hasNext
) {

    public static <T extends CreatedAtProvider> CreatedAtCursorResult<T> of(
        List<T> values, LocalDateTime cursor, int limit
    ) {
        boolean hasPrevious = checkFirstPageByCreatedAt(cursor, values);

        boolean hasNext = values.size() > limit;
        List<T> trimmedValues = hasNext ? values.subList(0, limit) : values;

        return new CreatedAtCursorResult<>(trimmedValues, hasPrevious, hasNext);
    }

    private static <T extends CreatedAtProvider> boolean checkFirstPageByCreatedAt(
        LocalDateTime cursor, List<T> values
    ) {
        return cursor != null
            && !values.isEmpty()
            && values.getFirst().getCreatedAt().isAfter(cursor);
    }
}
