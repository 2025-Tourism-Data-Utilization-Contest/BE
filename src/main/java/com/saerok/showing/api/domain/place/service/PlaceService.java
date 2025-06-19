package com.saerok.showing.api.domain.place.service;

import com.saerok.showing.api.domain.place.dto.response.PlaceDetailResponse;
import com.saerok.showing.api.domain.place.entity.Place;
import com.saerok.showing.api.domain.place.repository.PlaceRepository;
import com.saerok.showing.api.domain.review.entity.ReviewTargetType;
import com.saerok.showing.api.domain.review.service.ReviewService;
import com.saerok.showing.api.domain.theme.dto.response.ThemeSummaryResponse;
import com.saerok.showing.api.domain.theme.entity.Theme;
import com.saerok.showing.api.domain.theme.service.ThemeQueryService;
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
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final ReviewService reviewService;
    private final ThemeQueryService themeQueryService;

    private static final int NEARBY_RADIUS_METERS = 30000;  // 반경 30km
    private static final int NEARBY_LIMIT = 5;              // 최대 개수 제한

    @Transactional(readOnly = true)
    public PlaceDetailResponse getPlace(Long placeId) {
        Place place = findById(placeId);
        int reviewCount = reviewService.getReviewCount(ReviewTargetType.PLACE, placeId);
        List<Theme> nearbyThemes = getNearbyThemesFromPlace(placeId, NEARBY_RADIUS_METERS, NEARBY_LIMIT);
        List<ThemeSummaryResponse> themes = nearbyThemes.stream()
            .map(ThemeSummaryResponse::toDto)
            .collect(Collectors.toList());
        return PlaceDetailResponse.toDto(place, reviewCount, themes);
    }

    @Transactional(readOnly = true)
    public List<Theme> getNearbyThemesFromPlace(Long placeId, int radius, int limit) {
        Place place = findById(placeId);
        DistanceUtil.BoundingBox box = DistanceUtil.getBoundingBox(place.getLocationX(), place.getLocationY(), radius);
        List<Theme> candidates = themeQueryService.findThemesInArea(box.minLat(), box.maxLat(), box.minLon(),
            box.maxLon());

        return DistanceUtil.filterNearbyPositions(
            candidates, place.getLocationX(), place.getLocationY(), radius, limit
        );
    }

    private Place findById(Long placeId) {
        return placeRepository.findById(placeId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.PLACE_NOT_FOUND));
    }
}
