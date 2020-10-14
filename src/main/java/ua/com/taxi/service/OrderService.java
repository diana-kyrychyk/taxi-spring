package ua.com.taxi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.taxi.domain.dto.order.OrderListDto;
import ua.com.taxi.repository.filter.OrderFilters;

public interface OrderService {

    Page<OrderListDto> findAllDto(OrderFilters orderFilters, Pageable pageable);
}
