package com.saerok.showing.api.global.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DistanceUtil {

    private static final double EARTH_RADIUS_M = 6371000; // meters

    // 두 좌표 간 거리 계산
    public static double calculateDistance(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_M * c;
    }

    // 반경 기준 사각형 박스 계산
    public static BoundingBox getBoundingBox(double lon, double lat, double radiusMeters) {
        double latDelta = radiusMeters / 111000.0;
        double lonDelta = radiusMeters / (111000.0 * Math.cos(Math.toRadians(lat)));
        return new BoundingBox(
            lat - latDelta, lat + latDelta,
            lon - lonDelta, lon + lonDelta
        );
    }

    // 반경 내 가까운 후보지들 리스트 반환
    public static <T extends Locatable> List<T> filterNearbyPositions(
        List<T> candidates,
        double baseX,
        double baseY,
        int radiusMeters,
        int limit
    ) {
        return candidates.stream()
            .map(item -> new WithDistance<>(item,
                calculateDistance(baseX, baseY, item.getLocationX(), item.getLocationY())))
            .filter(wd -> wd.distance <= radiusMeters)
            .sorted(Comparator.comparingDouble(wd -> wd.distance))
            .limit(limit)
            .map(wd -> wd.item)
            .collect(Collectors.toList());
    }

    public record BoundingBox(double minLat, double maxLat, double minLon, double maxLon) { }

    private record WithDistance<T>(T item, double distance) { }
}
