package com.saerok.showing.api.domain.place.repository;

import com.saerok.showing.api.domain.place.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("""
            SELECT p FROM Place p
            WHERE p.locationY BETWEEN :minLat AND :maxLat
              AND p.locationX BETWEEN :minLon AND :maxLon
        """)
    List<Place> findPlacesInArea(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLon") double minLon,
        @Param("maxLon") double maxLon
    );
}
