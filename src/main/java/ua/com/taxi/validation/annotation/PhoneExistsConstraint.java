package ua.com.taxi.validation.annotation;

import ua.com.taxi.validation.validator.PhoneExistsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Constraint(validatedBy = PhoneExistsValidator.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface PhoneExistsConstraint {

    String message() default "{taxi.validation.constraints.phone.exists.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
