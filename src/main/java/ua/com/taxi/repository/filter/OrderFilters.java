package ua.com.taxi.repository.filter;

import org.springframework.format.annotation.DateTimeFormat;
import ua.com.taxi.validation.annotation.DateOrderConstraint;

import java.time.LocalDate;

@DateOrderConstraint(fieldName = "startDate")
public class OrderFilters {

    private Integer selectedPassenger;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public Integer getSelectedPassenger() {
        return selectedPassenger;
    }

    public void setSelectedPassenger(Integer selectedPassenger) {
        this.selectedPassenger = selectedPassenger;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "OrderFilters{" +
                "selectedPassenger=" + selectedPassenger +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
