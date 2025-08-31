package com.saerok.showing.api.domain.routePlace.entity;

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

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(name = "day_number", nullable = false)
    private int dayNumber;

    @Column(name = "order_in_day", nullable = false)
    private int orderInDay;

    @Column(name = "content_id", nullable = false)
    private String contentId;

    @Column(name = "content_type_id", nullable = false)
    private String contentTypeId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false)
    private String address;

    public static RoutePlace toEntity(RoutePlaceCreateRequest request, Route route, String PlaceName) {
        return RoutePlace.builder()
            .route(route)
            .placeName(PlaceName)
            .dayNumber(request.getDayNumber())
            .orderInDay(request.getOrderInDay())
            .contentId(request.getContentId())
            .contentTypeId(request.getContentTypeId())
            .imageUrl(request.getImageUrl())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .address(request.getAddress())
            .build();
    }

    public void update(RoutePlaceUpdateRequest request) {
        this.dayNumber = request.getDayNumber();
        this.orderInDay = request.getOrderInDay();
        this.placeName = request.getPlaceName();
    }

    // 같은 day 내 임시 파킹 (유니크 충돌 방지용)
    public void parkOrder() {
        this.orderInDay = -1;
    }

    // 같은 day 내에서 새로운 order로 이동
    public void placeAt(int newOrder) {
        this.orderInDay = newOrder;
    }

    // 다른 day로 이동하며 order도 설정
    public void moveTo(int newDay, int newOrder) {
        this.dayNumber = newDay;
        this.orderInDay = newOrder;
    }

    public void validateBelongsTo(Route route) {
        if (!this.route.getId().equals(route.getId())) {
            throw ShowingException.from(ErrorCode.ROUTE_MAKER_MISMATCH);
        }
    }
}
