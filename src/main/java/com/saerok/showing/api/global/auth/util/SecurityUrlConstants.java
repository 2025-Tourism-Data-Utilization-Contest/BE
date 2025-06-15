package com.saerok.showing.api.global.auth.util;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUrlConstants {

    public static final String[] PUBLIC_URLS = merge(
        Docs.URLS,
        OAuth2.URLS,
        Common.URLS
    );

    public static class Docs {
        public static final String[] URLS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/showing/docs/**",
            "/showing/swagger-ui/**",
            "/webjars/**",
            "/api-docs/**"
        };
    }

    public static class OAuth2 {
        public static final String[] URLS = {
            "/oauth2/**",
            "/login/oauth2/**",
            "/reissue",
            "/login/**",
        };
    }

    public static class Common {
        public static final String[] URLS = {
            "/actuator/health", "/error", "/favicon.ico", "/"
        };
    }

    private static String[] merge(String[]... arrays) {
        return Arrays.stream(arrays).flatMap(Arrays::stream).distinct().toArray(String[]::new);
    }
}
