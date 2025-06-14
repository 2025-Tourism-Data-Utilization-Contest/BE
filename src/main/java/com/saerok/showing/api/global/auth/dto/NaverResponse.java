package com.saerok.showing.api.global.auth.dto;

import com.saerok.showing.api.global.utils.CustomMapUtil;
import com.saerok.showing.api.global.utils.CustomStringUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> naverAccountMap;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = CustomMapUtil.getNestedMap(naverAccountMap, "response");
        return CustomStringUtil.toStringOrNull(response.get("email"));
    }

    @Override
    public String getName() {
        Map<String, Object> response = CustomMapUtil.getNestedMap(naverAccountMap, "response");
        return CustomStringUtil.toStringOrDefault(response.get("name"), "unknown");
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> response = CustomMapUtil.getNestedMap(naverAccountMap, "response");
        return CustomStringUtil.toStringOrNull(response.get("profile_image"));
    }
}
