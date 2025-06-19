package com.saerok.showing.api.domain.place.service;

import com.saerok.showing.api.domain.place.entity.Place;
import com.saerok.showing.api.domain.place.repository.PlaceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceQueryService {

    private final PlaceRepository placeRepository;

    @Transactional(readOnly = true)
    public List<Place> findPlacesInArea(double minLat, double maxLat, double minLon, double maxLon) {
        return placeRepository.findPlacesInArea(minLat, maxLat, minLon, maxLon);
    }
}
