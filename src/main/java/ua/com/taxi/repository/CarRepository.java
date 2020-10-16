package ua.com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.taxi.domain.Car;
import ua.com.taxi.domain.CarStatus;
import ua.com.taxi.domain.Category;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    Optional<Car> findFirstByCategoryAndStatusAndCapacityGreaterThanEqual(Category category, CarStatus status, int passengersCount);
}
