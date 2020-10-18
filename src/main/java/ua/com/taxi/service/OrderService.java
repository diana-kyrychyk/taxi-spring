package ua.com.taxi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.taxi.domain.Order;
import ua.com.taxi.domain.OrderStatus;
import ua.com.taxi.domain.dto.order.OrderConfirmDto;
import ua.com.taxi.domain.dto.order.OrderCreateDto;
import ua.com.taxi.domain.dto.order.OrderListDto;
import ua.com.taxi.repository.filter.OrderFilters;

import java.util.Optional;

public interface OrderService {

    Page<OrderListDto> findAllDto(OrderFilters orderFilters, Pageable pageable);

    int create(OrderCreateDto order);

    Optional<Order> findByUserAndStatus(int userId, OrderStatus status);

    Optional<OrderConfirmDto> prepareConfirm(int id);

    void cancel(int orderId);

    void confirm(int orderId, int carId);

    void complete(int orderId);
}
