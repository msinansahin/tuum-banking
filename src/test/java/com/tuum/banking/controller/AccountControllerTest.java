package com.tuum.banking.controller;

import static com.tuum.banking.common.RestApi.API_URI_V1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.banking.common.Country;
import com.tuum.banking.common.Currency;
import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.AccountCreateRequest;
import com.tuum.banking.model.dto.AccountDto;
import com.tuum.banking.model.dto.BalanceDto;
import com.tuum.banking.model.dto.TransactionCreateRequest;
import com.tuum.banking.model.dto.TransactionDto;
import com.tuum.banking.service.AccountService;
import com.tuum.banking.service.BalanceService;
import com.tuum.banking.service.TransactionService;

import lombok.SneakyThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {

    private static final Long accountId = 1L;
    private static final Long customerId = 2L;
    private static final Long transactionId = 2L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private BalanceService balanceService;

    @SneakyThrows
    @Test
    void createAccount__created() {
        var accountCreateRequest = new AccountCreateRequest()
                .setCountry(Country.EST)
                .setCustomerId(customerId)
                .setCurrencies(List.of(Currency.EUR.name(), Currency.SEK.name()));
        var accountDto = new AccountDto()
                .setAccountId(accountId)
                .setCustomerId(customerId)
                .setBalances(List.of(
                        new BalanceDto().setAmount(BigDecimal.ZERO).setCurrency(Currency.EUR),
                        new BalanceDto().setAmount(BigDecimal.ZERO).setCurrency(Currency.SEK)
                ));
        when(accountService.createAccount(accountCreateRequest)).thenReturn(accountDto);

        mockMvc.perform(post(API_URI_V1 + "/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(accountDto)));
        verify(accountService).createAccount(accountCreateRequest);
    }

    @SneakyThrows
    @Test
    void createAccount__bad_request__invalid_currency() {
        var requestJson = "{\"customerId\":\"2\",\"country\":\"EST\",\"currencies\":[\"TRY\",\"SEK\"]}";
        var accountDto = new AccountDto()
                .setAccountId(accountId)
                .setCustomerId(customerId)
                .setBalances(List.of(
                        new BalanceDto().setAmount(BigDecimal.ZERO).setCurrency(Currency.EUR),
                        new BalanceDto().setAmount(BigDecimal.ZERO).setCurrency(Currency.SEK)
                ));
        when(accountService.createAccount(any())).thenReturn(accountDto);

        mockMvc.perform(post(API_URI_V1 + "/account")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid currency"));
        verify(accountService, never()).createAccount(any());
    }

    @Test
    void getAccount() throws Exception {
        var accountDto = new AccountDto()
                .setAccountId(accountId)
                .setCustomerId(customerId)
                .setBalances(List.of(
                        new BalanceDto().setAmount(BigDecimal.valueOf(1000.50)).setCurrency(Currency.EUR),
                        new BalanceDto().setAmount(BigDecimal.valueOf(-50.45)).setCurrency(Currency.SEK)
                ));
        when(accountService.getAccount(accountId)).thenReturn(Optional.of(accountDto));
        mockMvc.perform(get(API_URI_V1 + "/account/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(accountDto)));
        verify(accountService).getAccount(accountId);
    }

    @SneakyThrows
    @Test
    void createTransaction__created() {
        var transactionCreateRequest = new TransactionCreateRequest()
                .setAccountId(accountId)
                .setCurrency(Currency.EUR.name())
                .setDescription("new transaction desc")
                .setDirection(Transaction.Direction.IN.name())
                .setAmount(BigDecimal.ONE);
        var transactionDto = new TransactionDto()
                .setTransactionId(transactionId)
                .setAccountId(accountId)
                .setCurrency(Currency.EUR)
                .setAmount(BigDecimal.ONE)
                .setDirection(Transaction.Direction.IN)
                .setDescription("new transaction desc")
                .setBalance(BigDecimal.TEN);
        var accountDto = new AccountDto().setAccountId(accountId);
        when(transactionService.createTransaction(transactionCreateRequest)).thenReturn(transactionDto);
        when(accountService.getAccount(accountId)).thenReturn(Optional.of(accountDto));

        mockMvc.perform(post(API_URI_V1 + "/account/{accountId}/transaction", accountId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transactionCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(transactionDto)));
        verify(transactionService).createTransaction(transactionCreateRequest);
    }

    @SneakyThrows
    @Test
    void createTransaction__invalid_account() {
        var transactionCreateRequest = new TransactionCreateRequest()
                .setAccountId(accountId)
                .setCurrency(Currency.EUR.name())
                .setDescription("new transaction desc")
                .setDirection(Transaction.Direction.IN.name())
                .setAmount(BigDecimal.ONE);
        when(accountService.getAccount(accountId)).thenReturn(Optional.empty());

        mockMvc.perform(post(API_URI_V1 + "/account/{accountId}/transaction", accountId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transactionCreateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid account"));
        verifyNoInteractions(transactionService);
    }

    @SneakyThrows
    @Test
    void readTransactions__invalid_account() {
        when(accountService.getAccount(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get(API_URI_V1 + "/account/1/transaction?page=0&size=10"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid account"));
        verifyNoInteractions(transactionService);
    }

}
