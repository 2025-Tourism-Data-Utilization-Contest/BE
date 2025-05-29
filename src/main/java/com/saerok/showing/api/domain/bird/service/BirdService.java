package com.saerok.showing.api.domain.bird.service;

import com.saerok.showing.api.domain.bird.repository.BirdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BirdService {

    private final BirdRepository birdRepository;
}
