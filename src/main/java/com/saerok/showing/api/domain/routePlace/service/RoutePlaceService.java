package com.saerok.showing.api.domain.routePlace.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.route.service.RouteService;
import com.saerok.showing.api.domain.routePlace.dto.request.RoutePlaceCreateRequest;
import com.saerok.showing.api.domain.routePlace.dto.request.RoutePlaceUpdateRequest;
import com.saerok.showing.api.domain.routePlace.dto.response.RoutePlaceListResponse;
import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import com.saerok.showing.api.domain.routePlace.repository.RoutePlaceRepository;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutePlaceService {

    private final RoutePlaceRepository routePlaceRepository;
    private final RouteService routeService;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long addRoutePlace(Long routeId, RoutePlaceCreateRequest request) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        shiftOrderIfDuplicate(routeId, request.getDayNumber(), request.getOrderInDay());
        RoutePlace routePlace = RoutePlace.toEntity(request, route, request.getPlaceName());
        routePlaceRepository.save(routePlace);
        return routePlace.getId();
    }

    @Transactional(readOnly = true)
    public RoutePlaceListResponse getRoutePlaces(Long routeId) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        List<RoutePlace> places = findByRouteOrderByDayNumberAscOrderInDayAsc(route);
        return RoutePlaceListResponse.toDto(route, places);
    }

    @Transactional
    public Long updateRoutePlace(Long routeId, RoutePlaceUpdateRequest request) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        RoutePlace routePlace = findByName(request.getPlaceName());
        if (isUpdateRequired(routePlace, request)) {
            reorderForUpdate(routeId, routePlace, request);
            shiftOrderIfDuplicate(routeId, request.getDayNumber(), request.getOrderInDay());
        }
        routePlace.validateBelongsTo(route);
        routePlace.update(request);
        return routePlace.getId();
    }

    @Transactional
    public Long deleteRoutePlace(Long routeId, int dayNumber, int orderInDay) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        RoutePlace routePlace = findRoutePlace(routeId, dayNumber, orderInDay);
        Long routePlaceId = routePlace.getId();
        routePlace.validateBelongsTo(route);
        routePlaceRepository.delete(routePlace);
        routePlaceRepository.shiftOrdersBackwardAfterDelete(routeId, dayNumber, orderInDay); // 삭제된 장소 뒤의 장소들 재정렬
        return routePlaceId;
    }

    @Transactional(readOnly = true)
    public List<RoutePlace> findByRouteOrderByDayNumberAscOrderInDayAsc(Route route) {
        return routePlaceRepository.findByRouteOrderByDayNumberAscOrderInDayAsc(route);
    }

    @Transactional(readOnly = true)
    public RoutePlace findByName(String name) {
        return routePlaceRepository.findByPlaceName(name)
            .orElseThrow(() -> ShowingException.from(ErrorCode.ROUTE_PLACE_NOT_FOUND));
    }

    private RoutePlace findRoutePlace(Long routeId, int dayNumber, int orderInDay) {
        return routePlaceRepository.findByRouteIdAndDayNumberAndOrderInDay(routeId, dayNumber, orderInDay)
            .orElseThrow(() -> ShowingException.from(ErrorCode.ROUTE_PLACE_NOT_FOUND));
    }

    private void reorderForUpdate(Long routeId, RoutePlace routePlace, RoutePlaceUpdateRequest req) {
        int oldDay = routePlace.getDayNumber();
        int oldOrder = routePlace.getOrderInDay();
        int newDay = req.getDayNumber();
        int newOrder = req.getOrderInDay();

        // 1. 이동 여부 확인
        if (oldDay == newDay && oldOrder == newOrder) return;
        // 2. 임시 파킹(유니크 제약 조건 충돌 방지)
        routePlace.parkOrder();
        // 3. 순서 재배치
        if (oldDay == newDay) {
            // 3-1. 같은 일차 내 이동
            if (newOrder > oldOrder) {
                shiftRangeBackwardWithinDay(routeId, oldDay, oldOrder, newOrder);
            } else {
                shiftRangeForwardWithinDay(routeId, oldDay, newOrder, oldOrder);
            }
            routePlace.placeAt(newOrder);
        } else {
            // 3-2. 다른 일차로 이동
            routePlaceRepository.shiftOrdersBackwardAfterDelete(routeId, oldDay, oldOrder);
            routePlaceRepository.shiftOrdersForward(routeId, newDay, newOrder);
            routePlace.moveTo(newDay, newOrder);
        }
    }

    private void shiftRangeBackwardWithinDay(Long routeId, int dayNumber, int oldOrder, int newOrder) {
        routePlaceRepository.shiftRangeBackwardWithinDay(routeId, dayNumber, oldOrder, newOrder);
    }

    private void shiftRangeForwardWithinDay(Long routeId, int dayNumber, int newOrder, int oldOrder) {
        routePlaceRepository.shiftRangeForwardWithinDay(routeId, dayNumber, newOrder, oldOrder);
    }

    private void shiftOrderIfDuplicate(Long routeId, int dayNumber, int orderInDay) {
        boolean exists = routePlaceRepository.existsByRouteIdAndDayNumberAndOrderInDay(routeId, dayNumber, orderInDay);
        if (exists) {
            routePlaceRepository.shiftOrdersForward(routeId, dayNumber, orderInDay);
        }
    }

    private boolean isUpdateRequired(RoutePlace routePlace, RoutePlaceUpdateRequest request) {
        return routePlace.getDayNumber() != request.getDayNumber() ||
            routePlace.getOrderInDay() != request.getOrderInDay();
    }

    private Route findRouteWithOwnerValidation(Long routeId, Member currentMember) {
        Route route = routeService.findById(routeId);
        route.validateOwner(currentMember);
        return route;
    }

    private RoutePlace findById(Long routePlaceId) {
        return routePlaceRepository.findById(routePlaceId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.ROUTE_PLACE_NOT_FOUND));
    }
}
