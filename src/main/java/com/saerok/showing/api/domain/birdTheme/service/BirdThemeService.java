package com.saerok.showing.api.domain.birdTheme.service;

import com.saerok.showing.api.domain.birdTheme.dto.response.BirdThemeResponse;
import com.saerok.showing.api.domain.birdTheme.respository.BirdThemeRepository;
import com.saerok.showing.api.domain.theme.dto.response.ThemeSummaryResponse;
import com.saerok.showing.api.domain.theme.entity.Theme;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BirdThemeService {

    private final BirdThemeRepository birdThemeRepository;

    @Transactional(readOnly = true)
    public List<BirdThemeResponse> getBirdsByTheme(Long themeId) {
        return birdThemeRepository.findByThemeId(themeId).stream()
            .map(BirdThemeResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ThemeSummaryResponse> getThemeNames(Long birdId) {
        List<Theme> themes = birdThemeRepository.findThemesByBirdId(birdId);
        return themes.stream()
            .map(ThemeSummaryResponse::toDto)
            .collect(Collectors.toList());
    }
}
