package ua.com.taxi.util;

import ua.com.taxi.domain.Point;

public class MapService {

    private MapService() {
    }

    public static long calculateDistance(Point point1, Point point2) {
        double R = 6371e3; // metres
        double φ1 = point1.getLatitude() * Math.PI / 180; // φ, λ in radians
        double φ2 = point2.getLatitude() * Math.PI / 180;
        double Δφ = (point2.getLatitude() - point1.getLatitude()) * Math.PI / 180;
        double Δλ = (point2.getLongtitude() - point1.getLongtitude()) * Math.PI / 180;

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        return (long) distance;
    }
}
