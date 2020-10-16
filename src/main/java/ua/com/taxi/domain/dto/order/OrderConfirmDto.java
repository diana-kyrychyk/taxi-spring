package ua.com.taxi.domain.dto.order;

import ua.com.taxi.domain.Car;
import ua.com.taxi.domain.Category;

public class OrderConfirmDto {

    private Integer orderId;
    private String userPhone;
    private String departureAddress;
    private String arrivalAddress;
    private Category category;
    private Integer passengersCount;
    private Long fare;
    private Long finalFare;
    private Car suggestedCar;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(String departureAddress) {
        this.departureAddress = departureAddress;
    }

    public String getArrivalAddress() {
        return arrivalAddress;
    }

    public void setArrivalAddress(String arrivalAddress) {
        this.arrivalAddress = arrivalAddress;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(Integer passengersCount) {
        this.passengersCount = passengersCount;
    }

    public Long getFare() {
        return fare;
    }

    public void setFare(Long fare) {
        this.fare = fare;
    }

    public Long getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(Long finalFare) {
        this.finalFare = finalFare;
    }

    public Car getSuggestedCar() {
        return suggestedCar;
    }

    public void setSuggestedCar(Car suggestedCar) {
        this.suggestedCar = suggestedCar;
    }
}
