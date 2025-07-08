package com.saerok.showing.api.domain.routePlace.repository;

import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePlaceRepository extends JpaRepository<RoutePlace, Long> {

    List<RoutePlace> findByRouteOrderByDayNumberAscOrderInDayAsc(Route route);
}
