package ua.com.taxi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.com.taxi.domain.Address;
import ua.com.taxi.domain.Order;
import ua.com.taxi.domain.dto.order.OrderListDto;
import ua.com.taxi.repository.OrderRepository;
import ua.com.taxi.repository.filter.OrderFilters;
import ua.com.taxi.repository.filter.OrderSpecification;
import ua.com.taxi.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<OrderListDto> findAllDto(OrderFilters orderFilters, Pageable pageable) {
        OrderSpecification orderSpecification = new OrderSpecification(orderFilters);
        Page<Order> ordersPage = orderRepository.findAll(orderSpecification, pageable);

        List<OrderListDto> dtoList = ordersPage.stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, ordersPage.getTotalElements());
    }

    private OrderListDto toListDto(Order order) {
        OrderListDto dto = new OrderListDto();
        dto.setOrderId(order.getId());
        dto.setUserPhone(order.getUser().getPhone());
        dto.setDepartureAddress(extractAddress(order.getDepartureAddress()));
        dto.setArrivalAddress(extractAddress(order.getArrivalAddress()));
        dto.setCategory(order.getCategory());
        dto.setLicensePlate(order.getCar() != null ? order.getCar().getLicensePlate() : null);
        dto.setFare(order.getFare());
        dto.setCreationDate(order.getCreationDate());
        dto.setStatus(order.getStatus());
        return dto;
    }

    private String extractAddress(Address address) {
        return address.getStreet().concat(", ").concat(address.getBuilding());
    }
}
