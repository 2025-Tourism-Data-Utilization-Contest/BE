package com.saerok.showing.api.domain.route.controller;

import com.saerok.showing.api.domain.route.dto.request.RouteCreateRequest;
import com.saerok.showing.api.domain.route.dto.request.RouteUpdateRequest;
import com.saerok.showing.api.domain.route.service.RouteService;
import com.saerok.showing.api.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
@Tag(name = "Route", description = "여행코스 관리")
public class RouteController {

    private final RouteService routeService;

    @Operation(
        summary = "여행코스 생성",
        description = """
            [모든 Role 가능] 새로운 여행코스를 생성합니다.<br>
            여행코스에는 제목, 여행 시작일자, 종료일자, 인원수가 필요합니다.<br>
            코스 생성 시에는 장소가 포함되지 않으며, 이후 별도로 추가해야 합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("")
    public ApiResponse<Long> createRoute(
        @Valid @RequestBody RouteCreateRequest request
    ) {
        Long id = routeService.save(request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "여행코스 수정",
        description = """
            [모든 Role 가능] 여행코스의 제목을 수정합니다.<br>
            본인이 생성한 여행코스만 수정할 수 있으며, 1~50자 길이의 여행코스 제목으로만 수정이 가능합니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @PatchMapping("/{routeId}")
    public ApiResponse<Long> updateRoute(
        @PathVariable(name = "routeId") Long routeId,
        @Valid @RequestBody RouteUpdateRequest request
    ) {
        Long id = routeService.update(routeId, request);
        return ApiResponse.success(id);
    }

    @Operation(
        summary = "여행코스 삭제",
        description = """
            [모든 Role 가능] 여행코스 ID를 기준으로 해당 코스를 삭제합니다.<br>
            해당 여행코스 내 장소들도 함께 삭제됩니다.
            """
    )
    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping("/{routeId}")
    public ApiResponse<Long> deleteRoute(
        @PathVariable(name = "routeId") Long routeId
    ) {
        Long id = routeService.delete(routeId);
        return ApiResponse.success(id);
    }
}
