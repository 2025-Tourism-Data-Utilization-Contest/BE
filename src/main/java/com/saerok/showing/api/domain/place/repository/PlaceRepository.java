package com.saerok.showing.api.domain.place.repository;

import com.saerok.showing.api.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository  extends JpaRepository<Place, Long> {

}
