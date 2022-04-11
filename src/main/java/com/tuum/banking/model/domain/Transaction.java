package com.tuum.banking.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Transaction extends AbstractEntity {

    @NotNull
    private Long accountId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Currency currency;
    @NotNull
    private Direction direction;

    @Size(max = 500)
    private String description;

    public enum Direction {
        IN, OUT
    }

}
