package com.saerok.showing.api.domain.routePlace.repository;

import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long> {

    Optional<RoutePlace> findByRouteIdAndDayNumberAndOrderInDay(Long routeId, int dayNumber, int orderInDay);

    List<RoutePlace> findByRouteOrderByDayNumberAscOrderInDayAsc(Route route);

    @Modifying(clearAutomatically = true)
    @Query("""
            update RoutePlace rp
               set rp.orderInDay = rp.orderInDay - 1
             where rp.route.id = :routeId
               and rp.dayNumber = :dayNumber
               and rp.orderInDay > :oldOrder
               and rp.orderInDay <= :newOrder
        """)
    void shiftRangeBackwardWithinDay(@Param("routeId") Long routeId,
        @Param("dayNumber") int dayNumber,
        @Param("oldOrder") int oldOrder,
        @Param("newOrder") int newOrder);

    @Modifying(clearAutomatically = true)
    @Query("""
            update RoutePlace rp
               set rp.orderInDay = rp.orderInDay + 1
             where rp.route.id = :routeId
               and rp.dayNumber = :dayNumber
               and rp.orderInDay >= :newOrder
               and rp.orderInDay < :oldOrder
        """)
    void shiftRangeForwardWithinDay(@Param("routeId") Long routeId,
        @Param("dayNumber") int dayNumber,
        @Param("newOrder") int newOrder,
        @Param("oldOrder") int oldOrder);

    @Modifying
    @Query("""
        update RoutePlace rp
           set rp.orderInDay = rp.orderInDay - 1
         where rp.route.id = :routeId
           and rp.dayNumber = :dayNumber
           and rp.orderInDay > :deletedOrder
        """)
    void shiftOrdersBackwardAfterDelete(@Param("routeId") Long routeId,
        @Param("dayNumber") int dayNumber,
        @Param("deletedOrder") int deletedOrder);

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

    Optional<RoutePlace> findByPlaceName(String name);
}
