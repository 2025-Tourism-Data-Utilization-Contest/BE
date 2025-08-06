package com.saerok.showing.api.global.pagination.provider;

import java.time.LocalDateTime;

public interface PopularProvider {

    int getLikeCount();

    LocalDateTime getCreatedAt();
}
