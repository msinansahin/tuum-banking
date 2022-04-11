package com.tuum.banking.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.TransactionDto;

@ExtendWith(MockitoExtension.class)
class ObjectMapperServiceTest {

    private static final long accountId = 1L;

    @InjectMocks
    private ObjectMapperService subject;

    @Test
    void transactionToDto() {
        var transaction = Transaction.builder()
                .id(222L)
                .accountId(accountId)
                .amount(BigDecimal.TEN)
                .currency(Currency.EUR)
                .direction(Transaction.Direction.IN)
                .description("desc")
                .build();
        var expectedDto = new TransactionDto()
                .setAccountId(accountId)
                .setTransactionId(222L)
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.EUR)
                .setDirection(Transaction.Direction.IN)
                .setDescription("desc");
        assertThat(subject.transactionToDto(transaction)).isEqualTo(expectedDto);
    }
}
