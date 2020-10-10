package ua.com.taxi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.UserRegistrationDto;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    Optional<User> create(UserRegistrationDto user);
}
