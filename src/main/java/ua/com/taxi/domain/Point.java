package ua.com.taxi.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Point {

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longtitude")
    private double longtitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }
}
