package ua.com.taxi.validation.validator;

import ua.com.taxi.repository.filter.OrderFilters;
import ua.com.taxi.validation.annotation.DateOrderConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateOrderListValidator implements ConstraintValidator<DateOrderConstraint, OrderFilters> {

    private String messagesTemplate = "";
    private String fieldName = "";

    @Override
    public void initialize(DateOrderConstraint constraintAnnotation) {
        messagesTemplate = constraintAnnotation.message();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(OrderFilters orderFilters, ConstraintValidatorContext context) {
        boolean result = isValidDates(orderFilters.getStartDate(), orderFilters.getEndDate());
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messagesTemplate)
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
        }
        return result;
    }

    private boolean isValidDates(LocalDate start, LocalDate end) {
        boolean result = true;
        if (start != null && end != null && end.isBefore(start)) {
            result = false;
        }
        return result;
    }
}
