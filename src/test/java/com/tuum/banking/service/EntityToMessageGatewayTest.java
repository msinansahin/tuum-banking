package com.tuum.banking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.model.domain.Transaction;

@ExtendWith(MockitoExtension.class)
class EntityToMessageGatewayTest {

    @InjectMocks
    private EntityToMessageGateway subject;

    @Mock private MessageSenderService messageSenderService;

    @Test
    void findConsumer__account() {
        var entity = new Account();
        subject.findConsumer(entity).accept(entity);
        verify(messageSenderService).sendAccount(entity);
    }

    @Test
    void findConsumer__transaction() {
        var entity = new Transaction();
        subject.findConsumer(entity).accept(entity);
        verify(messageSenderService).sendTransaction(entity);
    }

    @Test
    void findConsumer__balance() {
        var entity = new Balance();
        subject.findConsumer(entity).accept(entity);
        verifyNoInteractions(messageSenderService);
    }

}
