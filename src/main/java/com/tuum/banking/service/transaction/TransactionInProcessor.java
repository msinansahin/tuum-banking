package com.tuum.banking.service.transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.TransactionCreateRequest;
import com.tuum.banking.repository.TransactionRepository;
import com.tuum.banking.service.BalanceService;
import com.tuum.banking.service.ObjectMapperService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionInProcessor implements TransactionProcessor {

    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;
    private final ObjectMapperService objectMapperService;

    @Override
    @Transactional
    public TransactionResult process(TransactionCreateRequest request) {
        var transaction = objectMapperService.requestToTransaction(request);
        transactionRepository.insert(transaction);
        var currentBalance = balanceService.increaseBalance(request.getAccountId(), Currency.valueOf(request.getCurrency()), request.getAmount());
        return new TransactionResult(transaction, currentBalance);
    }

}
