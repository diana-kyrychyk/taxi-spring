package ua.com.taxi.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.taxi.domain.Role;
import ua.com.taxi.domain.User;
import ua.com.taxi.domain.dto.user.UserUpdateDto;
import ua.com.taxi.repository.RoleRepository;
import ua.com.taxi.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    int id = 1;
    String name = "testName";
    String phone = "+380630000001";
    String password = "passwordTest";
    List<Role> roles = buildRoles();

    @Mock
    UserRepository userRepositoryMock;
    @Mock
    RoleRepository roleRepositoryMock;
    @Mock
    PasswordEncoder passwordEncoderMock;
    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void setUpBeforeClass() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindUserByPhone() {
        User user = buildUser();
        when(userRepositoryMock.findByPhone(phone)).thenReturn(Optional.ofNullable(user));

        Optional<User> actualUser = userService.findByPhone(phone);

        assertTrue(actualUser.isPresent());
        assertEquals(roles, actualUser.get().getRoles());
    }

    @Test
    public void shouldFindUserForUpdate() {
        User user = buildUser();
        UserUpdateDto expectedUserUpdateDto = buildUserUpdateDto();
        when(userRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(user));

        UserUpdateDto actualUserUpdate = userService.findForUpdate(id);

        assertEquals(expectedUserUpdateDto, actualUserUpdate);
    }


    private UserUpdateDto buildUserUpdateDto() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(id);
        userUpdateDto.setName(name);
        userUpdateDto.setRoles(roles);
        return userUpdateDto;
    }

    private User buildUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }

    private List<Role> buildRoles() {
        Role role = new Role();
        role.setName(Role.ADMIN);
        return Arrays.asList(role);
    }
}
