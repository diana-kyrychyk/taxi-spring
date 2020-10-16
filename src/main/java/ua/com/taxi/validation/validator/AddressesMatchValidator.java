package ua.com.taxi.validation.validator;

import ua.com.taxi.domain.dto.order.OrderCreateDto;
import ua.com.taxi.validation.annotation.AddressesMatchConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressesMatchValidator implements ConstraintValidator<AddressesMatchConstraint, OrderCreateDto> {

    private String messagesTemplate = "";
    private String fieldName = "";

    @Override
    public void initialize(AddressesMatchConstraint constraintAnnotation) {
        messagesTemplate = constraintAnnotation.message();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(OrderCreateDto order, ConstraintValidatorContext context) {

        boolean result = !order.getArrivalId().equals(order.getDepartureId());
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messagesTemplate)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
        }
        return result;
    }
}
