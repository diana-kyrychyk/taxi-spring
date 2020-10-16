package ua.com.taxi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.taxi.domain.Address;
import ua.com.taxi.domain.Car;
import ua.com.taxi.domain.CarStatus;
import ua.com.taxi.domain.Order;
import ua.com.taxi.domain.OrderStatus;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.order.OrderConfirmDto;
import ua.com.taxi.domain.dto.order.OrderCreateDto;
import ua.com.taxi.domain.dto.order.OrderListDto;
import ua.com.taxi.exception.CarBusyException;
import ua.com.taxi.repository.AddressRepository;
import ua.com.taxi.repository.CarRepository;
import ua.com.taxi.repository.OrderRepository;
import ua.com.taxi.repository.UserRepository;
import ua.com.taxi.repository.filter.OrderFilters;
import ua.com.taxi.repository.filter.OrderSpecification;
import ua.com.taxi.service.OrderService;
import ua.com.taxi.util.FareCalculator;
import ua.com.taxi.util.MapService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private CarRepository carRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, AddressRepository addressRepository, CarRepository carRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.carRepository = carRepository;
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

    @Override
    public int create(OrderCreateDto orderDto) {
        Order order = buildOrder(orderDto);
        order = orderRepository.save(order);
        return order.getId();
    }

    private Order buildOrder(OrderCreateDto orderDto) {
        Optional<User> userOptional = userRepository.findById(orderDto.getUserId());
        Optional<Address> departureAddressOpt = addressRepository.findById(orderDto.getDepartureId());
        Optional<Address> arrivalAddressOpt = addressRepository.findById(orderDto.getArrivalId());

        User user = userOptional.orElseThrow(EntityNotFoundException::new);
        Address addressDeparture = departureAddressOpt.orElseThrow(EntityNotFoundException::new);
        Address addressArrival = arrivalAddressOpt.orElseThrow(EntityNotFoundException::new);

        Order order = new Order();
        order.setUser(user);
        order.setDiscount(user.getDiscount());
        order.setDepartureAddress(addressDeparture);
        order.setArrivalAddress(addressArrival);
        order.setPassengersCount(orderDto.getPassengerCount());
        order.setCategory(orderDto.getCategory());

        order.setFare(calculateFare(order));

        order.setFinalFare(calculateFinalFare(order));
        order.setCreationDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);
        return order;
    }

    private long calculateFare(Order order) {
        long distance = MapService.calculateDistance(order.getDepartureAddress().getPoint(), order.getArrivalAddress().getPoint());
        return FareCalculator.calculate(order.getCategory(), distance);
    }

    private long calculateFinalFare(Order order) {
        long discountCoins = order.getFare() * order.getDiscount() / 100;
        return order.getFare() - discountCoins;
    }

    @Override
    public Optional<Order> findByUserAndStatus(int userId, OrderStatus status) {
        return orderRepository.findFirstByUserIdAndStatus(userId, status);
    }

    @Override
    @Transactional
    public Optional<OrderConfirmDto> prepareConfirm(int id) {
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        Optional<Car> foundCar = carRepository.findFirstByCategoryAndStatusAndCapacityGreaterThanEqual(order.getCategory(), CarStatus.FREE, order.getPassengersCount());
        Car car = foundCar.orElse(null);

        OrderConfirmDto orderConfirm = buildConfirmDto(order, car);

        return Optional.of(orderConfirm);
    }

    private OrderConfirmDto buildConfirmDto(Order order, Car car) {
        OrderConfirmDto orderConfirm = new OrderConfirmDto();
        orderConfirm.setOrderId(order.getId());
        orderConfirm.setUserPhone(order.getUser().getPhone());
        orderConfirm.setArrivalAddress(order.getArrivalAddress().getStreet().concat(", ").concat(order.getArrivalAddress().getBuilding()));
        orderConfirm.setDepartureAddress(order.getDepartureAddress().getStreet().concat(", ").concat(order.getDepartureAddress().getBuilding()));
        orderConfirm.setCategory(order.getCategory());
        orderConfirm.setPassengersCount(order.getPassengersCount());
        orderConfirm.setFare(order.getFare());
        orderConfirm.setFinalFare(order.getFinalFare());
        orderConfirm.setSuggestedCar(car);
        return orderConfirm;
    }

    @Transactional
    @Override
    public void cancel(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.setStatus(OrderStatus.CANCELLED);

        if (order.getCar() != null) {
            Car car = order.getCar();
            car.setStatus(CarStatus.FREE);
        }
        orderRepository.save(order);
    }

    @Override
    public void confirm(int orderId, int carId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(EntityNotFoundException::new);

        if (!CarStatus.FREE.equals(car.getStatus())) {
            String message = String.format("Car '%s' is busy", carId);
            throw new CarBusyException(message);
        }

        car.setStatus(CarStatus.ON_ORDER);
        order.setCar(car);
        order.setDriver(car.getDriver());
        order.setStatus(OrderStatus.ON_ROAD);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void complete(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.setStatus(OrderStatus.COMPLETED);

        if (order.getCar() != null) {
            Car car = order.getCar();
            car.setStatus(CarStatus.FREE);
        }
        orderRepository.save(order);
    }
}
