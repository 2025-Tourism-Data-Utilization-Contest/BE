package com.saerok.showing.api.global.pagination.parser;

import com.saerok.showing.api.domain.post.entity.PostSortType;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.pagination.cursor.CreatedAtCursor;
import com.saerok.showing.api.global.pagination.cursor.PopularCursor;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CursorParser {

    public Object parse(String rawCursor, PostSortType sortType) {
        if (rawCursor == null || rawCursor.isBlank()) {
            return null;
        }

        try {
            return switch (sortType) {
                case LATEST -> parseCreatedAt(rawCursor);
                case POPULAR -> parsePopular(rawCursor);
            };
        } catch (Exception e) {
            throw ShowingException.from(ErrorCode.INVALID_CURSOR_FORMAT);
        }
    }

    private CreatedAtCursor parseCreatedAt(String rawCursor) {
        return new CreatedAtCursor(LocalDateTime.parse(rawCursor));
    }

    private PopularCursor parsePopular(String rawCursor) {
        String[] parts = rawCursor.split(",");
        if (parts.length != 2) {
            throw ShowingException.from(ErrorCode.INVALID_CURSOR_FORMAT);
        }
        int likeCount = Integer.parseInt(parts[0]);
        LocalDateTime createdAt = LocalDateTime.parse(parts[1]);
        return new PopularCursor(likeCount, createdAt);
    }
}
