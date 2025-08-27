package com.saerok.showing.api.domain.routePlace.controller;

import com.saerok.showing.api.domain.routePlace.dto.request.RoutePlaceCreateRequest;
import com.saerok.showing.api.domain.routePlace.dto.request.RoutePlaceUpdateRequest;
import com.saerok.showing.api.domain.routePlace.dto.response.RoutePlaceListResponse;
import com.saerok.showing.api.domain.routePlace.service.RoutePlaceService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
@Tag(name = "RoutePlace", description = "여행코스 내부 장소 관리")
public class RoutePlaceController {

    private final RoutePlaceService routePlaceService;

    @Operation(
        summary = "여행코스 내 장소 추가",
        description = """
            [모든 Role 가능] 여행코스에 장소를 추가합니다.<br>
            장소는 방문 일자(dayNumber)와 해당 일자 내 방문 순서(orderInDay)를 함께 지정해야 합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/{routeId}/places")
    public ApiResponse<Long> addRoutePlace(
        @PathVariable Long routeId,
        @Valid @RequestBody RoutePlaceCreateRequest request
    ) {
        Long createdId = routePlaceService.addRoutePlace(routeId, request);
        return ApiResponse.success(createdId);
    }

    @Operation(
        summary = "여행코스 일차별 장소 목록 조회",
        description = """
            [모든 Role 가능] 여행코스 내에 등록된 장소들을 일차(dayNumber)와 방문 순서(orderInDay)에 따라 정렬하여 조회합니다.<br>
            반환 데이터는 일차별로 그룹화되어 있습니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/{routeId}/places")
    public ApiResponse<RoutePlaceListResponse> getRoutePlaces(
        @PathVariable Long routeId
    ) {
        RoutePlaceListResponse response = routePlaceService.getRoutePlaces(routeId);
        return ApiResponse.success(response);
    }

    @Operation(
        summary = "여행코스 내 장소 수정",
        description = """
            [모든 Role 가능] 여행코스 내 특정 장소의 방문 일자(dayNumber) 또는 해당 일자 내 방문 순서(orderInDay)를 수정합니다.<br>
            route ID와 routePlace ID 모두 필요합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("/{routeId}/places")
    public ApiResponse<Long> updateRoutePlace(
        @PathVariable Long routeId,
        @Valid @RequestBody RoutePlaceUpdateRequest request
    ) {
        Long updatedId = routePlaceService.updateRoutePlace(routeId, request);
        return ApiResponse.success(updatedId);
    }

    @Operation(
        summary = "여행코스 내 장소 삭제",
        description = """
            [모든 Role 가능] 특정 여행코스에서 지정한 장소(routePlace)를 삭제합니다.<br>
            장소는 여행 일자(dayNumber)와 해당 일자의 순서(orderInDay)를 입력받아 삭제합니다.<br>
            장소를 삭제하면 해당 일차의 나머지 순서에는 영향을 주지 않습니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{routeId}/places")
    public ApiResponse<Long> deleteRoutePlace(
        @PathVariable Long routeId,
        @RequestParam int dayNumber,
        @RequestParam int orderInDay

    ) {
        Long deletedId = routePlaceService.deleteRoutePlace(routeId, dayNumber, orderInDay);
        return ApiResponse.success(deletedId);
    }
}
