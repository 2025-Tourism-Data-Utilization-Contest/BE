package com.saerok.showing.api.domain.routePlace.entity;

import com.saerok.showing.api.domain.place.entity.Place;
import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.routePlace.dto.request.RoutePlaceCreateRequest;
import com.saerok.showing.api.domain.routePlace.dto.request.RoutePlaceUpdateRequest;
import com.saerok.showing.api.global.entity.BaseEntity;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "route_place")
public class RoutePlace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_place_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;

    @Column(name = "order_in_day", nullable = false)
    private int orderInDay;

    public static RoutePlace toEntity(RoutePlaceCreateRequest request, Route route, Place place) {
        return RoutePlace.builder()
            .route(route)
            .place(place)
            .dayNumber(request.getDayNumber())
            .orderInDay(request.getOrderInDay())
            .build();
    }

    public void update(RoutePlaceUpdateRequest request) {
        this.dayNumber = request.getDayNumber();
        this.orderInDay = request.getOrderInDay();
    }

    public void validateBelongsTo(Route route) {
        if (!this.route.getId().equals(route.getId())) {
            throw ShowingException.from(ErrorCode.ROUTE_MAKER_MISMATCH);
        }
    }
}
