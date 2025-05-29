package com.saerok.showing.api.domain.bird.controller;

import com.saerok.showing.api.domain.bird.service.BirdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bird")
@RequiredArgsConstructor
@Tag(name = "Bird", description = "새 관리")
public class BirdController {

    private final BirdService birdService;
}
