package ua.com.taxi.domain.dto.order;

import ua.com.taxi.domain.Category;
import ua.com.taxi.validation.annotation.AddressesMatchConstraint;

@AddressesMatchConstraint(fieldName = "arrivalId", message = "{taxi.validation.constraints.addresses.message}")
public class OrderCreateDto {

    private int userId;
    private Category category;
    private int passengerCount;
    private Integer departureId;
    private Integer arrivalId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public Integer getDepartureId() {
        return departureId;
    }

    public void setDepartureId(Integer departureId) {
        this.departureId = departureId;
    }

    public Integer getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(Integer arrivalId) {
        this.arrivalId = arrivalId;
    }
}
