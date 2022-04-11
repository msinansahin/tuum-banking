package com.tuum.banking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuum.banking.common.Country;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.model.dto.AccountCreateRequest;
import com.tuum.banking.model.dto.AccountDto;
import com.tuum.banking.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Spy
    @InjectMocks
    private AccountService subject;

    @Mock private AccountRepository accountRepository;
    @Mock private BalanceService balanceService;
    @Mock private ObjectMapperService objectMapperService;

    @Test
    void createAccount() {
        var customerId = 1L;
        var request = new AccountCreateRequest()
                .setCountry(Country.EST)
                .setCustomerId(customerId)
                .setCurrencies(List.of(Currency.EUR.name(), Currency.USD.name()));
        var accountDto = mock(AccountDto.class);
        when(objectMapperService.accountToDto(any(Account.class))).thenReturn(accountDto);
        var balances = List.of(
                new Balance().setAmount(BigDecimal.ZERO).setCurrency(Currency.EUR),
                new Balance().setAmount(BigDecimal.ZERO).setCurrency(Currency.USD)
        );

        var test = subject.createAccount(request);
        assertThat(test).isEqualTo(accountDto);
        verify(accountRepository).insert(any(Account.class));
        verify(balanceService).insert(balances);
    }

    @Test
    void getAccount() {
        var accountId = 1L;
        var account = mock(Account.class);
        var accountDto = mock(AccountDto.class);
        var balances = mock(List.class);
        when(accountDto.setBalances(any())).thenReturn(accountDto);
        when(accountRepository.findById(accountId)).thenReturn(account);
        when(objectMapperService.accountToDto(any(Account.class))).thenReturn(accountDto);
        when(balanceService.findBalancesByAccountId(accountId)).thenReturn(balances);

        var test = subject.getAccount(accountId);
        assertThat(test).hasValue(accountDto);
        verify(accountDto).setBalances(balances);
    }
}
