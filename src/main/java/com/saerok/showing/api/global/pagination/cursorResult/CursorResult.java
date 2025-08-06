package com.saerok.showing.api.global.pagination.cursorResult;

import java.util.List;

public interface CursorResult<T> {

    List<T> values();

    boolean hasPrevious();

    boolean hasNext();
}
