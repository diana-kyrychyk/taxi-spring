package ua.com.taxi.validation.annotation;

import ua.com.taxi.validation.validator.PasswordsMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface PasswordsMatch {

    String message() default "{taxi.validation.constraints.password.confirm.message}";

    String fieldName();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
