package ua.com.taxi.validation.validator;

import ua.com.taxi.domain.dto.UserRegistrationDto;
import ua.com.taxi.validation.annotation.PasswordsMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDto> {

    private String messagesTemplate = "";
    private String fieldName = "";

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        messagesTemplate = constraintAnnotation.message();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(UserRegistrationDto user, ConstraintValidatorContext context) {

        boolean result = user.getPassword().equals(user.getPasswordConfirm());
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messagesTemplate)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
        }
        return result;
    }
}
