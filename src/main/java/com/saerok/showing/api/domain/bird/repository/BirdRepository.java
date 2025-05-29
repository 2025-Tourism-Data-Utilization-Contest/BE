package com.saerok.showing.api.domain.bird.repository;

import com.saerok.showing.api.domain.bird.entity.Bird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirdRepository extends JpaRepository<Bird, Long> {

}
