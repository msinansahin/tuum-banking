package com.tuum.banking.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.tuum.banking.RabbitmqIntegrationTest;
import com.tuum.banking.config.BankingProperties;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Balance;
import com.tuum.banking.model.domain.Transaction;

import lombok.SneakyThrows;

@ActiveProfiles("test")
@RabbitmqIntegrationTest
class MessageSenderServiceIntegrationTest {

    @Autowired
    private MessageSenderService messageSenderService;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private BankingProperties bankingProperties;

    @BeforeEach
    void setUp() {
        rabbitAdmin.purgeQueue(bankingProperties.getAccount().getQueueName(), false);
        rabbitAdmin.purgeQueue(bankingProperties.getTransaction().getQueueName(), false);
    }

    @AfterEach
    void tearDown() {
        rabbitAdmin.purgeQueue(bankingProperties.getAccount().getQueueName(), false);
        rabbitAdmin.purgeQueue(bankingProperties.getTransaction().getQueueName(), false);
    }

    @Test
    void sendAccount() {
        var account = Account.builder()
                .id(1L)
                .createdAt(LocalDateTime.now())
                .balances(List.of(new Balance()))
                .build();
        messageSenderService.sendAccount(account);
        messageSenderService.sendAccount(account);

        waitFor(1000, () -> assertThat(rabbitAdmin.getQueueInfo(bankingProperties.getAccount().getQueueName())
                .getMessageCount()).isEqualTo(2));

    }

    @SneakyThrows
    @Test
    void sendTransaction() {
        var transaction = Transaction.builder()
                .id(1L)
                .accountId(1L)
                .createdAt(LocalDateTime.now())
                .build();
        messageSenderService.sendTransaction(transaction);
        waitFor(1000, () -> assertThat(rabbitAdmin.getQueueInfo(bankingProperties.getTransaction().getQueueName())
                .getMessageCount()).isEqualTo(1));

    }

    @SneakyThrows
    private void waitFor(int wait, Runnable runnable) {
        Thread.sleep(wait);
        runnable.run();
    }

}
