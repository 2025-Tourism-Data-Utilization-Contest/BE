package com.saerok.showing.api.domain.birdTheme.service;

import com.saerok.showing.api.domain.bird.dto.response.BirdSummaryResponse;
import com.saerok.showing.api.domain.bird.entity.Bird;
import com.saerok.showing.api.domain.birdTheme.respository.BirdThemeRepository;
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
    public List<BirdSummaryResponse> getBirdNames(Long themeId) {
        List<Bird> birds = birdThemeRepository.findBirdsByThemeId(themeId);
        return birds.stream()
            .map(BirdSummaryResponse::toDto)
            .collect(Collectors.toList());
    }
}
