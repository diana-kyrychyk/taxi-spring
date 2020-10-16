package ua.com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ua.com.taxi.domain.Order;
import ua.com.taxi.domain.OrderStatus;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    Optional<Order> findFirstByUserIdAndStatus(int userId, OrderStatus status);
}
