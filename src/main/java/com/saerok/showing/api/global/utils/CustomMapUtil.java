package com.saerok.showing.api.global.utils;

import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomMapUtil {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNestedMap(Map<String, Object> source, String key) {
        Object value = source.get(key);
        return value instanceof Map ? (Map<String, Object>) value : Map.of();
    }
}
