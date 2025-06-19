package com.saerok.showing.api.domain.theme.service;

import com.saerok.showing.api.domain.bird.dto.response.BirdSummaryResponse;
import com.saerok.showing.api.domain.birdTheme.service.BirdThemeService;
import com.saerok.showing.api.domain.theme.dto.response.ThemeDetailResponse;
import com.saerok.showing.api.domain.theme.dto.response.ThemePinResponse;
import com.saerok.showing.api.domain.theme.entity.DayTime;
import com.saerok.showing.api.domain.theme.entity.Season;
import com.saerok.showing.api.domain.theme.entity.Theme;
import com.saerok.showing.api.domain.theme.repository.ThemeRepository;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final BirdThemeService birdThemeService;

    @Transactional(readOnly = true)
    public List<ThemePinResponse> getThemes(List<Season> seasons, List<DayTime> dayTimes) {
        List<Theme> themes = themeRepository.findBySeasonAndDayTime(seasons, dayTimes);
        return themes.stream()
            .map(ThemePinResponse::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ThemeDetailResponse getTheme(Long themeId) {
        Theme theme = findById(themeId);
        List<BirdSummaryResponse> birds = birdThemeService.getBirdNames(themeId);
        return ThemeDetailResponse.toDto(theme, birds);
    }

    private Theme findById(Long themeId) {
        return themeRepository.findById(themeId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.THEME_NOT_FOUND));
    }
}
