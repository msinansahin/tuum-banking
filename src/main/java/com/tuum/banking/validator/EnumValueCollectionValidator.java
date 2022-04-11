package com.tuum.banking.validator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.CollectionUtils;

public class EnumValueCollectionValidator implements ConstraintValidator<EnumValue, Collection<String>> {

    private List<String> validValues;

    @Override
    public void initialize(EnumValue annotation) {
        validValues = Stream.of(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(Collection<String> value, ConstraintValidatorContext context) {
        return CollectionUtils.isEmpty(value) || validValues.containsAll(value);
    }
}
