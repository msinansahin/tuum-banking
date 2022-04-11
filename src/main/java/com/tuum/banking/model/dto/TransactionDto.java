package com.tuum.banking.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Size;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction.Direction;

import lombok.Data;

@Data
public class TransactionDto implements Serializable {
    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private Currency currency;
    private Direction direction;
    @Size(max = 500)
    private String description;
    private BigDecimal balance;
}
