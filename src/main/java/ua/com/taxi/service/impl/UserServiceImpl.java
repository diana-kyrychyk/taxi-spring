package ua.com.taxi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.taxi.domain.Role;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.user.UserRegistrationDto;
import ua.com.taxi.domain.dto.user.UserUpdateDto;
import ua.com.taxi.repository.RoleRepository;
import ua.com.taxi.repository.UserRepository;
import ua.com.taxi.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ua.com.taxi.domain.Role.USER;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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
        LOGGER.debug("loadUserByUsername() [{}]", phone);
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException(phone));

        String[] roles = user.getRoles().stream()
                .map(role -> role.getName())
                .map(roleName -> "ROLE_".concat(roleName))
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
        LOGGER.debug("findByPhone() [{}]", phone);
        return userRepository.findByPhone(phone);
    }

    @Override
    public boolean existsByPhone(String phone) {
        LOGGER.debug("existsByPhone() [{}]", phone);
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Optional<User> create(UserRegistrationDto userDto) {
        LOGGER.debug("create() [{}] ", userDto);
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

    @Override
    public List<User> findAll() {
        LOGGER.debug("findAll()");
        return userRepository.findAll(Sort.by("name").ascending());
    }

    @Override
    public Optional<User> findById(int id) {
        LOGGER.debug("findById()");
        return userRepository.findById(id);
    }

    @Override
    public UserUpdateDto findForUpdate(int id) {
        LOGGER.debug("findForUpdate() [{}]", id);
        Optional<User> user = userRepository.findById(id);
        return toUpdateDto(user.orElseThrow(EntityNotFoundException::new));
    }

    private UserUpdateDto toUpdateDto(User user) {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(user.getId());
        userUpdateDto.setName(user.getName());
        userUpdateDto.setRoles(user.getRoles());
        return userUpdateDto;
    }

    @Transactional
    @Override
    public void update(UserUpdateDto userDto) {
        LOGGER.debug("update() [{}]", userDto);
        User user = toEntity(userDto);
        userRepository.save(user);
    }

    private User toEntity(UserUpdateDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(EntityNotFoundException::new);
        user.setName(userDto.getName());

        List<Role> roles = extractRoles(userDto.getRoles());
        user.setRoles(roles);
        return user;
    }

    private List<Role> extractRoles(List<Role> rolesDto) {
        List<Role> roles = new ArrayList<>();
        for (Role role : rolesDto) {
            Role roleFromDb = roleRepository.findById(role.getId()).orElseThrow(EntityNotFoundException::new);
            roles.add(roleFromDb);
        }
        return roles;
    }

    @Override
    public void rechargeBalance(String userPhone, long amount) {
        User user = userRepository.findByPhone(userPhone).orElseThrow(EntityNotFoundException::new);
        long newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        userRepository.save(user);
    }
}
