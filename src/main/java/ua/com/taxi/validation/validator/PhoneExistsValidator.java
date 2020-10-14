package ua.com.taxi.validation.validator;

import org.springframework.beans.factory.annotation.Autowired;
import ua.com.taxi.service.UserService;
import ua.com.taxi.validation.annotation.PhoneExistsConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneExistsValidator implements ConstraintValidator<PhoneExistsConstraint, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(PhoneExistsConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return !userService.existsByPhone(phone);
    }
}
