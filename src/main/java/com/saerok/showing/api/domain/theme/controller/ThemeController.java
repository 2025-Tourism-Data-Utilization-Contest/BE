package com.saerok.showing.api.domain.theme.controller;

import com.saerok.showing.api.domain.theme.service.ThemeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/theme")
@RequiredArgsConstructor
@Tag(name = "Theme", description = "테마 관리")
public class ThemeController {

    private final ThemeService themeService;
}
