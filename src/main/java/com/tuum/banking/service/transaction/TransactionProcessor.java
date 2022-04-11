package com.tuum.banking.service.transaction;

import java.math.BigDecimal;

import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.TransactionCreateRequest;

import lombok.Value;

public interface TransactionProcessor {

    TransactionResult process(TransactionCreateRequest request);

    @Value
    class TransactionResult {
        Transaction transaction;
        BigDecimal currentBalance;
    }
}
