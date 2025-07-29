package com.saerok.showing.api.domain.route.service;

import com.saerok.showing.api.domain.member.entity.Member;
import com.saerok.showing.api.domain.place.dto.response.PlaceSummaryResponse;
import com.saerok.showing.api.domain.route.dto.request.RouteCreateRequest;
import com.saerok.showing.api.domain.route.dto.response.RouteSummaryResponse;
import com.saerok.showing.api.domain.route.entity.Route;
import com.saerok.showing.api.domain.route.repository.RouteRepository;
import com.saerok.showing.api.domain.routePlace.entity.RoutePlace;
import com.saerok.showing.api.global.auth.util.LoginMemberProvider;
import com.saerok.showing.api.global.exception.ErrorCode;
import com.saerok.showing.api.global.exception.ShowingException;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final LoginMemberProvider loginMemberProvider;

    @Transactional
    public Long save(RouteCreateRequest request) {
        Member member = loginMemberProvider.getCurrentLoginMember();
        Route route = Route.toEntity(member, request);
        return routeRepository.save(route).getId();
    }

    @Transactional(readOnly = true)
    public List<RouteSummaryResponse> getMyRouteSummaries() {
        Member member = loginMemberProvider.getCurrentLoginMember();
        List<Route> routes = routeRepository.findByMemberId(member.getId());

        return routes.stream()
            .map(route -> {
                List<PlaceSummaryResponse> topPlaces = route.getRoutePlaces().stream()
                    .sorted(Comparator.comparingInt(RoutePlace::getDayNumber)
                        .thenComparingInt(RoutePlace::getOrderInDay))
                    .limit(3)
                    .map(routePlace -> PlaceSummaryResponse.create(routePlace.getPlace()))
                    .toList();

                return RouteSummaryResponse.toDto(route, topPlaces);
            })
            .toList();
    }

    @Transactional
    public Long delete(Long routeId) {
        Member currentMember = loginMemberProvider.getCurrentLoginMember();
        Route route = findById(routeId);
        route.validateOwner(currentMember);
        routeRepository.deleteById(routeId);
        return routeId;
    }

    public Route findById(Long routeId) {
        return routeRepository.findById(routeId)
            .orElseThrow(() -> ShowingException.from(ErrorCode.ROUTE_NOT_FOUND));
    }
}
