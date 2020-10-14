package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.order.OrderListDto;
import ua.com.taxi.domain.dto.order.SearchParameters;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.UserService;

import java.util.List;

@Controller
public class OrderController {

    private OrderService orderService;
    private UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/admin/order-list")
    public String getOrderList(Model model, @Validated SearchParameters searchParameters, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            resetDates(searchParameters);
        }
        Pageable pageable = specifyPageable(searchParameters);
        Page<OrderListDto> ordersPage = orderService.findAllDto(searchParameters.getOrderFilters(), pageable);
        fillSearchParams(searchParameters, ordersPage);

        model.addAttribute("orders", ordersPage.getContent());
        model.addAttribute("searchParameters", searchParameters);
        return "order/order-list";
    }


    private Pageable specifyPageable(SearchParameters searchParameters) {
        Sort sort = SearchParameters.SORT_BY_FARE.equals(searchParameters.getSortType())
                ? Sort.by("fare").descending()
                : Sort.by("creationDate").descending();

        return PageRequest.of(searchParameters.getPageNumber(), searchParameters.getPageSize(), sort);
    }

    private void fillSearchParams(SearchParameters searchParameters, Page<OrderListDto> ordersPage) {
        searchParameters.setPageNumber(ordersPage.getNumber());
        searchParameters.setTotalPages(ordersPage.getTotalPages());
        List<User> users = userService.findAll();
        searchParameters.setAllUsers(users);
    }

    private void resetDates(SearchParameters searchParameters) {
        searchParameters.getOrderFilters().setStartDate(null);
        searchParameters.getOrderFilters().setEndDate(null);
    }
}
