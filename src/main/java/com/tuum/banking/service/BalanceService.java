package com.tuum.banking.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuum.banking.common.BankingException;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.model.dto.BalanceDto;
import com.tuum.banking.repository.BalanceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final ObjectMapperService objectMapperService;

    @Transactional(readOnly = true)
    public Optional<BigDecimal> getBalanceAmount(Long accountId, Currency currency) {
        return getBalance(accountId, currency).map(BalanceDto::getAmount);
    }

    @Transactional(readOnly = true)
    public Optional<BalanceDto> getBalance(Long accountId, Currency currency) {
        return Optional.ofNullable(balanceRepository.findBalance(accountId, currency)).map(objectMapperService::balanceToDto);
    }

    @Transactional
    public void insert(List<Balance> balances) {
        balances.forEach(balanceRepository::insert);
    }

    @Transactional
    public BigDecimal increaseBalance(Long accountId, Currency currency, BigDecimal amount) {
        var currentBalance = expireBalance(accountId, currency);
        var newCurrentBalance = new Balance()
                .setCurrency(currentBalance.getCurrency())
                .setAccountId(accountId)
                .setAmount(currentBalance.getAmount().add(amount));
        balanceRepository.insert(newCurrentBalance);
        return newCurrentBalance.getAmount();
    }

    @Transactional
    public BigDecimal decreaseBalance(Long accountId, Currency currency, BigDecimal amount) {
        var currentBalanceAmount = getBalanceAmount(accountId, currency)
                .orElseThrow(() -> new BankingException("Current balance could not found"));
        if (currentBalanceAmount.compareTo(amount) < 0) {
            throw new BankingException("Insufficient funds");
        }
        var currentBalance = expireBalance(accountId, currency);
        var newCurrentBalance = new Balance()
                .setCurrency(currentBalance.getCurrency())
                .setAccountId(accountId)
                .setAmount(currentBalance.getAmount().subtract(amount));
        balanceRepository.insert(newCurrentBalance);
        return newCurrentBalance.getAmount();
    }

    protected Balance expireBalance(Long accountId, Currency currency) {
        var currentBalance = getBalance(accountId, currency)
                .map(BalanceDto::getId)
                .map(balanceRepository::findById)
                .orElseThrow(() -> new BankingException("Could not find current balance by %s/%s".formatted(accountId, currency)));
        balanceRepository.expireBalance(currentBalance.getId());
        return currentBalance;
    }

    @Transactional
    public List<BalanceDto> findBalancesByAccountId(Long accountId) {
        return balanceRepository.findBalancesByAccountId(accountId).stream()
                .map(objectMapperService::balanceToDto)
                .toList();
    }
}
