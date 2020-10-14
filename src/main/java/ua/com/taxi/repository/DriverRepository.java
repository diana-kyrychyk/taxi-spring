package ua.com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.taxi.domain.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
}
