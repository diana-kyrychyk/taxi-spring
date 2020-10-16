package ua.com.taxi.validation.annotation;

import ua.com.taxi.validation.validator.AddressesMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = AddressesMatchValidator.class)
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface AddressesMatchConstraint {

    String message() default "{taxi.validation.constraints.addresses.message}";

    String fieldName();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
