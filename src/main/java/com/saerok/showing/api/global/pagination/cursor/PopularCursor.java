package com.saerok.showing.api.global.pagination.cursor;

import java.time.LocalDateTime;

public record PopularCursor(
    int likeCount,
    LocalDateTime createdAt
) {}
