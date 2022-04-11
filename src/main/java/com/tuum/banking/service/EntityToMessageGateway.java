package com.tuum.banking.service;

import java.util.function.Consumer;

import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;

import com.tuum.banking.model.domain.AbstractEntity;
import com.tuum.banking.model.domain.Account;
import com.tuum.banking.model.domain.Transaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EntityToMessageGateway {

    private final MessageSenderService messageSenderService;
    private final Consumer<AbstractEntity> accountConsumer;
    private final Consumer<AbstractEntity> transactionConsumer;

    public EntityToMessageGateway(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
        this.accountConsumer = entity -> EntityToMessageGateway.this.messageSenderService.sendAccount((Account) entity);
        this.transactionConsumer = entity -> EntityToMessageGateway.this.messageSenderService.sendTransaction((Transaction) entity);
    }

    private final Consumer<AbstractEntity> emptyConsumer = entity -> log.debug("Not consumed entity: {}", entity);

    public <T extends AbstractEntity> void sendEntity(SqlCommandType sqlCommandType, T entity) {
        log.debug("Method:{}, entity will be processed for sending: {}", sqlCommandType, entity);
        findConsumer(entity).accept(entity);
    }

    protected Consumer<AbstractEntity> findConsumer(AbstractEntity entity) {
        final var clazz = entity.getClass().getSimpleName();
        return switch (clazz) {
            case "Account" -> accountConsumer;
            case "Transaction" -> transactionConsumer;
            default -> emptyConsumer;
        };
    }

}
