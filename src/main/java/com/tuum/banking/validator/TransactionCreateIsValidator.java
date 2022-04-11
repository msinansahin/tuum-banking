package com.tuum.banking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.tuum.banking.model.dto.TransactionCreateRequest;

@Component
public class TransactionCreateIsValidator implements ConstraintValidator<TransactionCreateIsValid, TransactionCreateRequest> {

    @Override
    public void initialize(TransactionCreateIsValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(TransactionCreateRequest value, ConstraintValidatorContext context) {
        var valid = true;
        String errorMessage = null;

        // TODO any custom validation?

        if (StringUtils.isNotBlank(errorMessage)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
            valid = false;
        }

        return valid;
    }

}
