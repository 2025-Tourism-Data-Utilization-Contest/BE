package com.saerok.showing.api.domain.bird.service;

import com.saerok.showing.api.domain.bird.dto.response.BirdDetailResponse;
import com.saerok.showing.api.domain.bird.entity.Bird;
import com.saerok.showing.api.domain.bird.repository.BirdRepository;
import com.saerok.showing.api.domain.birdTheme.service.BirdThemeService;
import com.saerok.showing.api.domain.theme.dto.response.ThemeSummaryResponse;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BirdService {

    private final BirdRepository birdRepository;
    private final BirdThemeService birdThemeService;

    @Transactional(readOnly = true)
    public BirdDetailResponse getBird(Long birdId){
        Bird bird = findById(birdId);
        List<ThemeSummaryResponse> themes = birdThemeService.getThemeNames(birdId);
        return BirdDetailResponse.toDto(bird, themes);
    }

    private Bird findById(Long birdId) {
        return birdRepository.findById(birdId)
            .orElseThrow(()-> ShowingException.from(ErrorCode.BIRD_NOT_FOUND));
    }
}
