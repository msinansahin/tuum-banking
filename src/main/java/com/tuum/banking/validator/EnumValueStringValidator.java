package com.tuum.banking.validator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueStringValidator implements ConstraintValidator<EnumValue, String> {

    private List<String> validValues;

    @Override
    public void initialize(EnumValue anno) {
        validValues = Stream.of(anno.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return validValues.contains(value);
    }
}
