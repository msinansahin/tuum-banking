package com.tuum.banking.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.tuum.banking.common.Currency;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Balance  extends AbstractEntity {

    @NotNull
    private Long accountId;

    @NotNull
    private Currency currency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDateTime validFrom;

    private LocalDateTime validUntil;
}
