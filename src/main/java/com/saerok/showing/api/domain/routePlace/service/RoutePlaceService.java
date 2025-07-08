package com.saerok.showing.api.domain.routePlace.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.place.entity.Place;
import com.saerok.showing.api.domain.place.service.PlaceService;
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
    private final PlaceService placeService;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long addRoutePlace(Long routeId, RoutePlaceCreateRequest request) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        Place place = placeService.findById(request.getPlaceId());
        shiftOrderIfDuplicate(routeId, request.getDayNumber(), request.getOrderInDay());
        RoutePlace routePlace = RoutePlace.toEntity(request, route, place);
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
    public Long updateRoutePlace(Long routeId, Long routePlaceId, RoutePlaceUpdateRequest request) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        RoutePlace routePlace = findById(routePlaceId);
        if (isUpdateRequired(routePlace, request)) {
            shiftOrderIfDuplicate(routeId, request.getDayNumber(), request.getOrderInDay());
        }
        routePlace.validateBelongsTo(route);
        routePlace.update(request);
        return routePlace.getId();
    }

    @Transactional
    public Long deleteRoutePlace(Long routeId, Long routePlaceId) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findRouteWithOwnerValidation(routeId, currentMember);
        RoutePlace routePlace = findById(routePlaceId);
        routePlace.validateBelongsTo(route);
        routePlaceRepository.delete(routePlace);
        return routePlaceId;
    }

    @Transactional(readOnly = true)
    public List<RoutePlace> findByRouteOrderByDayNumberAscOrderInDayAsc(Route route) {
        return routePlaceRepository.findByRouteOrderByDayNumberAscOrderInDayAsc(route);
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
