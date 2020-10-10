package ua.com.taxi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.taxi.domain.Role;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.UserRegistrationDto;
import ua.com.taxi.repository.RoleRepository;
import ua.com.taxi.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static ua.com.taxi.domain.Role.USER;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException(phone));

        String[] roles = user.getRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getPhone())
                .password(user.getPassword())
                .disabled(false)
                .authorities(roles)
                .build();
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Optional<User> create(UserRegistrationDto userDto) {
        User user = buildUser(userDto);
        user = userRepository.save(user);
        return Optional.of(user);
    }

    private User buildUser(UserRegistrationDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        String hashPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(hashPassword);
        user.setPhone(userDto.getPhone());

        Role role = roleRepository.findByName(USER).orElseThrow(EntityNotFoundException::new);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setRoles(roles);
        return user;
    }
}
