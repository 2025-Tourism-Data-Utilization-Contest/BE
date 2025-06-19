package com.saerok.showing.api.domain.theme.service;

import com.saerok.showing.api.domain.bird.dto.response.BirdSummaryResponse;
import com.saerok.showing.api.domain.birdTheme.service.BirdThemeService;
import com.saerok.showing.api.domain.place.dto.response.AttractionPlaceSummaryResponse;
import com.saerok.showing.api.domain.place.dto.response.ExperiencePlaceSummaryResponse;
import com.saerok.showing.api.domain.place.entity.Place;
import com.saerok.showing.api.domain.place.entity.PlaceType;
import com.saerok.showing.api.domain.place.service.PlaceQueryService;
import com.saerok.showing.api.domain.review.dto.response.ReviewSummaryResponse;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import com.saerok.showing.api.domain.review.service.ReviewService;
import com.saerok.showing.api.domain.theme.dto.response.ThemeDetailResponse;
import com.saerok.showing.api.domain.theme.dto.response.ThemePinResponse;
import com.saerok.showing.api.domain.theme.entity.DayTime;
import com.saerok.showing.api.domain.theme.entity.Season;
import com.saerok.showing.api.domain.theme.entity.Theme;
import com.saerok.showing.api.domain.theme.repository.ThemeRepository;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import com.saerok.showing.api.global.utils.DistanceUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReviewService reviewService;
    private final BirdThemeService birdThemeService;
    private final PlaceQueryService placeQueryService;

    private static final int NEARBY_RADIUS_METERS = 30000; // 반경 30km
    private static final int NEARBY_LIMIT = 5;              // 최대 개수 제한

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
        List<Place> nearbyPlaces = getNearbyPlacesFromTheme(themeId, NEARBY_RADIUS_METERS, NEARBY_LIMIT);

        // 필터링된 결과에서 유형별로 매핑
        List<AttractionPlaceSummaryResponse> attractionPlaces = nearbyPlaces.stream()
            .filter(p -> p.getPlaceType() == PlaceType.ATTRACTION)
            .map(AttractionPlaceSummaryResponse::toDto)
            .collect(Collectors.toList());

        List<ExperiencePlaceSummaryResponse> experiencePlaces = nearbyPlaces.stream()
            .filter(p -> p.getPlaceType() == PlaceType.EXPERIENCE)
            .map(ExperiencePlaceSummaryResponse::toDto)
            .collect(Collectors.toList());

        int reviewCount = reviewService.getReviewCount(ReviewTargetType.THEME, themeId);
        List<ReviewSummaryResponse> reviews = reviewService.getRecentReviews(ReviewTargetType.THEME, themeId);

        return ThemeDetailResponse.toDto(theme, birds, attractionPlaces, experiencePlaces, reviewCount, reviews);
    }

    @Transactional(readOnly = true)
    public List<Place> getNearbyPlacesFromTheme(Long themeId, int radius, int limit) {
        Theme theme = findById(themeId);
        DistanceUtil.BoundingBox box = DistanceUtil.getBoundingBox(theme.getLocationX(), theme.getLocationY(), radius);
        List<Place> candidates = placeQueryService.findPlacesInArea(box.minLat(), box.maxLat(), box.minLon(),
            box.maxLon());

        return DistanceUtil.filterNearbyPositions(
            candidates, theme.getLocationX(), theme.getLocationY(), radius, limit
        );
    }

    private Theme findById(Long themeId) {
        return themeRepository.findById(themeId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.THEME_NOT_FOUND));
    }
}
