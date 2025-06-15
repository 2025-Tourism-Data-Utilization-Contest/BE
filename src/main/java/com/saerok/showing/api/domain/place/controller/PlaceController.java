package com.saerok.showing.api.domain.place.controller;

import com.saerok.showing.api.domain.place.service.PlaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
@Tag(name = "Place", description = "장소 관리")
public class PlaceController {

    private final PlaceService placeService;
}
