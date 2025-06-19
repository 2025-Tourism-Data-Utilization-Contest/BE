package com.saerok.showing.api.domain.theme.service;

import com.saerok.showing.api.domain.theme.entity.Theme;
import com.saerok.showing.api.domain.theme.repository.ThemeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeQueryService {

    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<Theme> findThemesInArea(double minLat, double maxLat, double minLon, double maxLon) {
        return themeRepository.findThemesInArea(minLat, maxLat, minLon, maxLon);
    }
}
