package ua.com.taxi.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.user.UserRegistrationDto;
import ua.com.taxi.domain.dto.user.UserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<User> findByPhone(String phone);

    UserUpdateDto findForUpdate(int id);

    void update(UserUpdateDto user);

    Optional<User> findById(int id);

    boolean existsByPhone(String phone);

    Optional<User> create(UserRegistrationDto user);

    List<User> findAll();

    void rechargeBalance(String userPhone, long amount);
}
