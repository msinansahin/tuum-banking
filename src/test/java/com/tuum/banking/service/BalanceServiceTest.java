package com.tuum.banking.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuum.banking.common.BankingException;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.repository.BalanceRepository;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    private static final long accountId = 1L;

    @Spy
    @InjectMocks
    private BalanceService balanceService;

    @Mock private BalanceRepository balanceRepository;

    @Test
    void decreaseBalance() {
        doReturn(Optional.of(BigDecimal.valueOf(100))).when(balanceService).getBalanceAmount(accountId, Currency.EUR);
        doReturn(new Balance().setAmount(BigDecimal.valueOf(100))).when(balanceService).expireBalance(accountId, Currency.EUR);
        balanceService.decreaseBalance(accountId, Currency.EUR, BigDecimal.TEN);
        verify(balanceRepository).insert(any(Balance.class));
    }

    @Test
    void decreaseBalance__insufficient_funds() {
        doReturn(Optional.of(BigDecimal.ONE)).when(balanceService).getBalanceAmount(accountId, Currency.EUR);
        assertThrows(BankingException.class, () -> balanceService.decreaseBalance(accountId, Currency.EUR, BigDecimal.TEN));
    }
}
