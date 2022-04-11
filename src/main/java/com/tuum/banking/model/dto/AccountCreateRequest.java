package com.tuum.banking.model.dto;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tuum.banking.common.Country;
import com.tuum.banking.common.Currency;
import com.tuum.banking.validator.EnumValue;

import lombok.Data;

@Data
public class AccountCreateRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private Country country;
    @NotEmpty
    @EnumValue(enumClass = Currency.class, message = "Invalid currency")
    private List<String> currencies;
}
