package com.tuum.banking.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.tuum.banking.common.Country;

import lombok.Data;

@Data
public class AccountDto implements Serializable {
    private Long accountId;
    private Long customerId;
    private Country country;
    private List<BalanceDto> balances;
}
