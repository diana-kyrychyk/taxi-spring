package ua.com.taxi.domain.dto.order;

import ua.com.taxi.domain.Category;
import ua.com.taxi.domain.OrderStatus;

import java.time.LocalDateTime;

public class OrderListDto {

    private Integer orderId;
    private String userPhone;
    private String departureAddress;
    private String arrivalAddress;
    private Category category;
    private String licensePlate;
    private Long fare;
    private LocalDateTime creationDate;
    private OrderStatus status;

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

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Long getFare() {
        return fare;
    }

    public void setFare(Long fare) {
        this.fare = fare;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderListDto{" +
                "orderId=" + orderId +
                ", userPhone='" + userPhone + '\'' +
                ", departureAddress='" + departureAddress + '\'' +
                ", arrivalAddress='" + arrivalAddress + '\'' +
                ", category=" + category +
                ", licensePlate='" + licensePlate + '\'' +
                ", fare=" + fare +
                ", creationDate=" + creationDate +
                ", status=" + status +
                '}';
    }
}
