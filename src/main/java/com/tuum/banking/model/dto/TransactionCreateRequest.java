package com.tuum.banking.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction.Direction;
import com.tuum.banking.validator.EnumValue;
import com.tuum.banking.validator.TransactionCreateIsValid;

import lombok.Data;

@TransactionCreateIsValid
@Data
public class TransactionCreateRequest {

    @JsonIgnore
    private Long accountId;

    @Positive(message = "Invalid amount")
    private BigDecimal amount;

    @EnumValue(enumClass = Currency.class, message = "Invalid currency")
    private String currency;

    @EnumValue(enumClass = Direction.class, message = "Invalid direction")
    private String direction;

    @NotEmpty(message = "description missing")
    @Size(max = 500)
    private String description;

}
