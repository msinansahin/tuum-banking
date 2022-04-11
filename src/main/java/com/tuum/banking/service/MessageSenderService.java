package com.tuum.banking.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tuum.banking.config.BankingProperties;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
@Async("taskExecutorMessage")
public class MessageSenderService {

    private final RabbitTemplate rabbitTemplate;
    private final BankingProperties bankingProperties;
    private final ObjectMapperService objectMapperService;

    public void sendAccount(Account entity) {
        rabbitTemplate.convertAndSend(bankingProperties.getAccount().getExchangeName(),
                bankingProperties.getAccount().getRouteKey(),
                objectMapperService.accountToDto(entity));
        log.info("Account message is sent to: {}, by id: {}", bankingProperties.getAccount().getExchangeName(), entity.getId());
    }

    public void sendTransaction(Transaction entity) {
        rabbitTemplate.convertAndSend(bankingProperties.getTransaction().getExchangeName(),
                bankingProperties.getTransaction().getRouteKey(),
                objectMapperService.transactionToDto(entity));
        log.info("Transaction message is sent to: {}, by id: {}", bankingProperties.getTransaction().getExchangeName(), entity.getId());
    }
}
