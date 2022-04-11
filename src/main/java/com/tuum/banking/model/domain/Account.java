package com.tuum.banking.model.domain;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tuum.banking.common.Country;

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
@EqualsAndHashCode(callSuper = true, exclude = {"balances"})
public class Account extends AbstractEntity {

    @NotNull
    private Country country;

    @NotNull
    private Long customerId;

    @NotEmpty
    private List<Balance> balances;

    public List<Balance> getBalances() {
        return balances == null ? List.of() : balances;
    }

}
