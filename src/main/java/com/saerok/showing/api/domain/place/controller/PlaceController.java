package com.saerok.showing.api.domain.place.controller;

import com.saerok.showing.api.domain.place.dto.response.PlaceDetailResponse;
import com.saerok.showing.api.domain.place.service.PlaceService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
@Tag(name = "Place", description = "장소 관리")
public class PlaceController {

    private final PlaceService placeService;

    @Operation(
        summary = "단건 테마 조회",
        description = """
            [모든 Role 가능] placeId를 통해 한 장소(호텔, 축제, 체험 등)의 상세 정보를 조회합니다.
            장소
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{placeId}")
    public ApiResponse<PlaceDetailResponse> getPlace(
        @PathVariable Long placeId
    ) {
        PlaceDetailResponse place = placeService.getPlace(placeId);
        return ApiResponse.success(place);
    }
}
