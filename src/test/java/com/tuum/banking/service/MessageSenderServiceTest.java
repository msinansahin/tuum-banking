package com.tuum.banking.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.tuum.banking.config.BankingProperties;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.AccountDto;
import com.tuum.banking.model.dto.TransactionDto;

@ExtendWith(MockitoExtension.class)
class MessageSenderServiceTest {

    @InjectMocks
    private MessageSenderService subject;

    @Mock private BankingProperties bankingProperties;
    @Mock private RabbitTemplate rabbitTemplate;
    @Mock private ObjectMapperService objectMapperService;

    @Test
    void sendAccount() {
        var account = mock(Account.class);
        var accountDto = mock(AccountDto.class);
        when(bankingProperties.getAccount()).thenReturn(new BankingProperties.Account("exc-name", "q-name","route-key"));
        when(objectMapperService.accountToDto(account)).thenReturn(accountDto);
        subject.sendAccount(account);
        verify(rabbitTemplate).convertAndSend("exc-name", "route-key", accountDto);
    }

    @Test
    void sendTransaction() {
        var transaction = mock(Transaction.class);
        var transactionDto = mock(TransactionDto.class);
        when(bankingProperties.getTransaction()).thenReturn(new BankingProperties.Transaction("exc-name", "q-name", "route-key"));
        when(objectMapperService.transactionToDto(transaction)).thenReturn(transactionDto);
        subject.sendTransaction(transaction);
        verify(rabbitTemplate).convertAndSend("exc-name", "route-key", transactionDto);
    }
}
