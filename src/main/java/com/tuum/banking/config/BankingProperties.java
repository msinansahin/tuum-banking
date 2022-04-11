package com.tuum.banking.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

@RequiredArgsConstructor
@ToString
@Setter
@Getter
@ConstructorBinding
@Validated
@ConfigurationProperties(prefix = "banking")
public class BankingProperties {

    @NotNull
    private Account account;

    @NotNull
    private Transaction transaction;

    @Value
    public static class Account {
        @NotBlank
        String exchangeName;
        @NotBlank
        String queueName;
        @NotBlank
        String routeKey;
    }

    @Value
    public static class Transaction {
        @NotBlank
        String exchangeName;
        @NotBlank
        String queueName;
        @NotBlank
        String routeKey;
    }
}
