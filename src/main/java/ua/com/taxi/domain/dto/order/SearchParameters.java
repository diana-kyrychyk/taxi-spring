package ua.com.taxi.domain.dto.order;

import ua.com.taxi.domain.User;
import ua.com.taxi.repository.filter.OrderFilters;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchParameters {
    public static final String SORT_BY_DATE = "BY_DATE";
    public static final String SORT_BY_FARE = "BY_FARE";
    public static final List<String> ALL_SORT_TYPES = Arrays.asList(SORT_BY_DATE, SORT_BY_FARE);
    public static final String DEFAULT_SORT_TYPE = SORT_BY_DATE;
    public static final int DEFAULT_PAGE_SIZE = 8;

    private List<User> allUsers = new ArrayList<>();

    @Valid
    private OrderFilters orderFilters = new OrderFilters();

    private String sortType = DEFAULT_SORT_TYPE;

    private int pageSize = DEFAULT_PAGE_SIZE;
    private int pageNumber = 0;
    private int totalPages;


    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public OrderFilters getOrderFilters() {
        return orderFilters;
    }

    public void setOrderFilters(OrderFilters orderFilters) {
        this.orderFilters = orderFilters;
    }

    @Override
    public String toString() {
        return "SearchParameters{" +
                "allUsers=" + allUsers +
                ", orderFilters=" + orderFilters +
                ", sortType='" + sortType + '\'' +
                ", pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                ", totalPages=" + totalPages +
                '}';
    }
}
