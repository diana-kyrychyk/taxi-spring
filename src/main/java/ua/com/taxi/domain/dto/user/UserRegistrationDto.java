package ua.com.taxi.domain.dto.user;

import ua.com.taxi.validation.annotation.PasswordsMatch;
import ua.com.taxi.validation.annotation.PhoneExistsConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PasswordsMatch(fieldName = "passwordConfirm", message = "{taxi.validation.constraints.password.confirm.message}")
public class UserRegistrationDto {

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[+][0-9]{12}", message = "{taxi.validation.constraints.phone.pattern.message}")
    @PhoneExistsConstraint
    private String phone;

    @NotBlank
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "{taxi.validation.constraints.password.pattern.message}")
    private String password;

    private String passwordConfirm;

    public UserRegistrationDto() {
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Override
    public String toString() {
        return "UserRegistrationDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
