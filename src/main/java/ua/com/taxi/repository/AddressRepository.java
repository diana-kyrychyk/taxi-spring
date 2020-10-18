package ua.com.taxi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.taxi.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
