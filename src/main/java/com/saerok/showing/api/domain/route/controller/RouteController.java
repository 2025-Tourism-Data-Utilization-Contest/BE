package com.saerok.showing.api.domain.route.controller;

import com.saerok.showing.api.domain.route.service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
@Tag(name = "Route", description = "나만의 경로 관리")
public class RouteController {

    private final RouteService routeService;
}
