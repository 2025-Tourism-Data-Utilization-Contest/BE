package com.saerok.showing.api.domain.theme.controller;

import com.saerok.showing.api.domain.theme.dto.response.ThemeDetailResponse;
import com.saerok.showing.api.domain.theme.dto.response.ThemePinResponse;
import com.saerok.showing.api.domain.theme.entity.DayTime;
import com.saerok.showing.api.domain.theme.entity.Season;
import com.saerok.showing.api.domain.theme.service.ThemeService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/theme")
@RequiredArgsConstructor
@Tag(name = "Theme", description = "테마 관리")
public class ThemeController {

    private final ThemeService themeService;

    @Operation(
        summary = "조건별 테마 조회",
        description = "[모든 Role 가능] 계절과 시간대 조건으로 테마 목록을 조회합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("")
    public ApiResponse<List<ThemePinResponse>> getThemes(
        @RequestParam List<Season> seasons,
        @RequestParam List<DayTime> dayTimes
    ) {
        List<ThemePinResponse> themes = themeService.getThemes(seasons, dayTimes);
        return ApiResponse.success(themes);
    }

    @Operation(
        summary = "단건 테마 조회",
        description = "[모든 Role 가능] themeId를 통해 한 테마의 상세 정보를 조회합니다."
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{themeId}")
    public ApiResponse<ThemeDetailResponse> getTheme(@PathVariable Long themeId) {
        ThemeDetailResponse theme = themeService.getTheme(themeId);
        return ApiResponse.success(theme);
    }

}
