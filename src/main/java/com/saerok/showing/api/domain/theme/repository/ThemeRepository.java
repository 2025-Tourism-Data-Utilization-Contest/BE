package com.saerok.showing.api.domain.theme.repository;

import com.saerok.showing.api.domain.theme.entity.DayTime;
import com.saerok.showing.api.domain.theme.entity.Season;
import com.saerok.showing.api.domain.theme.entity.Theme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Query("SELECT DISTINCT t FROM Theme t JOIN t.seasons s JOIN t.dayTimes d " +
        "WHERE s IN :seasons AND d IN :dayTimes")
    List<Theme> findBySeasonAndDayTime(@Param("seasons") List<Season> seasons,
        @Param("dayTimes") List<DayTime> dayTimes);

    @Query("""
            SELECT t FROM Theme t
            WHERE t.locationY BETWEEN :minLat AND :maxLat
              AND t.locationX BETWEEN :minLon AND :maxLon
        """)
    List<Theme> findThemesInArea(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLon") double minLon,
        @Param("maxLon") double maxLon
    );
}
