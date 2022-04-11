package com.tuum.banking.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.domain.Transaction.Direction;
import com.tuum.banking.model.dto.AccountDto;
import com.tuum.banking.model.dto.BalanceDto;
import com.tuum.banking.model.dto.TransactionCreateRequest;
import com.tuum.banking.model.dto.TransactionDto;

@Service
public class ObjectMapperService {

    public AccountDto accountToDto(Account account) {
        return account == null
                ? null
                : new AccountDto()
                .setCountry(account.getCountry())
                .setAccountId(account.getId())
                .setCustomerId(account.getCustomerId())
                .setBalances(account.getBalances().stream().map(this::balanceToDto).filter(Objects::nonNull).toList());
    }

    public TransactionDto transactionToDto(Transaction transaction) {
        return transaction == null
            ? null
            : new TransactionDto()
                .setTransactionId(transaction.getId())
                .setAmount(transaction.getAmount())
                .setDirection(transaction.getDirection())
                .setDescription(transaction.getDescription())
                .setCurrency(transaction.getCurrency())
                .setAccountId(transaction.getAccountId());
    }

    public BalanceDto balanceToDto(Balance balance) {
        return balance == null
                ? null
                : new BalanceDto()
                .setId(balance.getId())
                .setAmount(balance.getAmount())
                .setCurrency(balance.getCurrency())
                .setValidFrom(balance.getValidFrom())
                .setValidUntil(balance.getValidUntil());
    }

    public Transaction requestToTransaction(TransactionCreateRequest request) {
        return request == null
                ? null
                : new Transaction()
                .setAccountId(request.getAccountId())
                .setAmount(request.getAmount())
                .setCurrency(Currency.valueOf(request.getCurrency()))
                .setDirection(Direction.valueOf(request.getDirection()))
                .setDescription(request.getDescription());
    }
}
