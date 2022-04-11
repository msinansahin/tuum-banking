package com.tuum.banking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.TransactionCreateRequest;
import com.tuum.banking.model.dto.TransactionDto;
import com.tuum.banking.repository.TransactionRepository;
import com.tuum.banking.service.transaction.TransactionProcessor;
import com.tuum.banking.service.transaction.TransactionProcessor.TransactionResult;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final long accountId = 1L;

    private TransactionService subject;

    @Mock private TransactionProcessor transactionInProcessor;
    @Mock private TransactionProcessor transactionOutProcessor;
    @Mock private ObjectMapperService objectMapperService;
    @Mock private TransactionRepository transactionRepository;

    @BeforeEach
    void setup() {
        subject = spy(new TransactionService(transactionInProcessor, transactionOutProcessor, objectMapperService, transactionRepository));
    }

    @ParameterizedTest
    @EnumSource(value = Transaction.Direction.class)
    void createTransaction(Transaction.Direction direction) {
        var request = new TransactionCreateRequest()
                .setAmount(BigDecimal.TEN)
                .setCurrency(Currency.EUR.name())
                .setDirection(direction.name())
                .setDescription("desc")
                .setAccountId(accountId);
        var transactionDto = spy(new TransactionDto());
        var processor = mock(TransactionProcessor.class);
        when(processor.process(request)).thenReturn(new TransactionResult(new Transaction(), BigDecimal.ONE));
        when(objectMapperService.transactionToDto(any(Transaction.class))).thenReturn(transactionDto);
        doReturn(processor).when(subject).getProcessor(direction);

        var test = subject.createTransaction(request);
        assertThat(test).isEqualTo(transactionDto);
        verify(processor).process(request);
    }

    @Test
    void getProcessor() {
        assertThat(subject.getProcessor(Transaction.Direction.IN)).isSameAs(transactionInProcessor);
        assertThat(subject.getProcessor(Transaction.Direction.OUT)).isSameAs(transactionOutProcessor);
    }

}
