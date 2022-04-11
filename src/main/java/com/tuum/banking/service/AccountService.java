package com.tuum.banking.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.model.dto.AccountCreateRequest;
import com.tuum.banking.model.dto.AccountDto;
import com.tuum.banking.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BalanceService balanceService;
    private final ObjectMapperService objectMapperService;

    @Transactional
    public AccountDto createAccount(AccountCreateRequest request) {
        var account = new Account()
                .setCountry(request.getCountry())
                .setCustomerId(request.getCustomerId());

        accountRepository.insert(account);
        var accountId = account.getId();
        var balances = request.getCurrencies().stream()
                .map(currency -> new Balance().setAccountId(accountId).setAmount(BigDecimal.ZERO).setCurrency(Currency.valueOf(currency)))
                .toList();
        balanceService.insert(balances);
        account.setBalances(balances);
        return objectMapperService.accountToDto(account);
    }

    @Transactional(readOnly = true)
    public Optional<AccountDto> getAccount(Long accountId) {
        return Optional
                .ofNullable(objectMapperService.accountToDto(accountRepository.findById(accountId)))
                .map(accountDto -> accountDto.setBalances(balanceService.findBalancesByAccountId(accountId)));
    }

}
