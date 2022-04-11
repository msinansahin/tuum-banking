package com.tuum.banking.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.tuum.banking.common.Currency;

import lombok.Data;

@Data
public class BalanceDto implements Serializable {
    private Long id;
    private BigDecimal amount;
    private Currency currency;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
}
