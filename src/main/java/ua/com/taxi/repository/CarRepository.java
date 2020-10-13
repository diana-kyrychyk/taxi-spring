package ua.com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.taxi.domain.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
}
