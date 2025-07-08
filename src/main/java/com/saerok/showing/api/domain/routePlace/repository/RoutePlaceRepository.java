package com.saerok.showing.api.domain.routePlace.repository;

import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long> {

    List<RoutePlace> findByRouteOrderByDayNumberAscOrderInDayAsc(Route route);

    @Modifying
    @Query("""
            UPDATE RoutePlace rp
            SET rp.orderInDay = rp.orderInDay + 1
            WHERE rp.route.id = :routeId
              AND rp.dayNumber = :dayNumber
              AND rp.orderInDay >= :startOrder
        """)
    void shiftOrdersForward(Long routeId, int dayNumber, int startOrder);

    boolean existsByRouteIdAndDayNumberAndOrderInDay(Long routeId, int dayNumber, int orderInDay);
}
