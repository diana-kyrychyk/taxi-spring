package ua.com.taxi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.taxi.domain.Address;
import ua.com.taxi.domain.Category;
import ua.com.taxi.domain.Order;
import ua.com.taxi.domain.OrderStatus;
import ua.com.taxi.domain.Role;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.order.OrderConfirmDto;
import ua.com.taxi.domain.dto.order.OrderCreateDto;
import ua.com.taxi.domain.dto.order.OrderListDto;
import ua.com.taxi.domain.dto.order.SearchParameters;
import ua.com.taxi.exception.CarBusyException;
import ua.com.taxi.service.AddressService;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
public class OrderController {

    private static final List<Integer> AVAILABLE_PASSENGER_COUNT_SETS = Arrays.asList(2, 4, 8);

    private MessageSource messageSource;

    private OrderService orderService;
    private UserService userService;
    private AddressService addressService;

    @Autowired
    public OrderController(MessageSource messageSource, OrderService orderService, UserService userService, AddressService addressService) {
        this.messageSource = messageSource;
        this.orderService = orderService;
        this.userService = userService;
        this.addressService = addressService;
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

    @GetMapping("/user/order-create")
    public String createOrderForm(Model model, Principal principal) {
        Optional<Order> newOrder = findCurrentNewOrder(principal);
        String page = "";

        if (newOrder.isPresent()) {
            page = String.format("redirect:/user/order-confirm?id=%s", newOrder.get().getId());
        } else {
            OrderCreateDto order = new OrderCreateDto();
            List<Address> availableAddresses = addressService.findAll();

            Category[] availableCategories = Category.values();
            model.addAttribute("order", order);
            model.addAttribute("availableAddresses", availableAddresses);
            model.addAttribute("availableCategories", availableCategories);
            model.addAttribute("passengerCounts", AVAILABLE_PASSENGER_COUNT_SETS);
            page = "order/order-creation";
        }
        return page;
    }

    private Optional<Order> findCurrentNewOrder(Principal principal) {
        String userPhone = principal.getName();
        User user = userService.findByPhone(userPhone).orElseThrow(EntityNotFoundException::new);
        return orderService.findByUserAndStatus(user.getId(), OrderStatus.NEW);
    }

    @PostMapping("/user/order-create")
    public String createOrder(@Valid @ModelAttribute("order") OrderCreateDto order,
                              BindingResult bindingResult, Principal principal, Model model) {

        String page = "";
        if (bindingResult.hasErrors()) {
            List<Address> availableAddresses = addressService.findAll();
            Category[] availableCategories = Category.values();
            model.addAttribute("availableAddresses", availableAddresses);
            model.addAttribute("availableCategories", availableCategories);
            model.addAttribute("passengerCounts", AVAILABLE_PASSENGER_COUNT_SETS);
            page = "/order/order-creation";
        } else {
            fillUser(order, principal);
            int newOrderId = orderService.create(order);
            page = "redirect:/user/order-confirm?id=".concat(String.valueOf(newOrderId));
        }
        return page;
    }

    private void fillUser(OrderCreateDto order, Principal principal) {
        String userPhone = principal.getName();
        User user = userService.findByPhone(userPhone).orElseThrow(EntityNotFoundException::new);
        order.setUserId(user.getId());
    }

    @GetMapping("/user/order-confirm")
    public String confirmOrder(Model model, @RequestParam("id") int id) {

        OrderConfirmDto order = orderService.prepareConfirm(id).orElseThrow(EntityNotFoundException::new);
        model.addAttribute("order", order);
        return "order/order-confirm";
    }

    @PostMapping("/user/order-confirm")
    public String userConfirmOrder(@RequestParam("orderId") int orderId, @RequestParam("carId") int carId,
                                   RedirectAttributes redirectAttributes, Locale locale) {

        String page = "";
        try {
            orderService.confirm(orderId, carId);
            page = "redirect:/";
        } catch (CarBusyException e) {
            String message = messageSource.getMessage("order-confirm.car-is-busy.exception", new Object[0], locale);
            redirectAttributes.addFlashAttribute("errorMessage", message);
            page = String.format("redirect:/user/order-confirm?id=%s", orderId);
        }
        return page;
    }

    @GetMapping({"/user/order-cancel", "/admin/order-cancel"})
    public String userCancelOrder(@RequestParam("id") int id) {
        orderService.cancel(id);
        String redirectPath = "";
        if (isAdmin()) {
            redirectPath = "redirect:/admin/order-list";
        } else {
            redirectPath = "redirect:/";
        }
        return redirectPath;
    }

    private boolean isAdmin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority("ROLE_".concat(Role.ADMIN)));
    }

    @GetMapping("/admin/order-complete")
    public String completeOrder(@RequestParam("id") int id) {
        orderService.complete(id);
        return "redirect:/admin/order-list";
    }
}
