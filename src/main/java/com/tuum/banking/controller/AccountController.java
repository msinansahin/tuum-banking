package com.tuum.banking.controller;

import static com.tuum.banking.common.RestApi.API_URI_V1;
import static org.springframework.http.HttpStatus.CREATED;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tuum.banking.common.BankingException;
import com.tuum.banking.model.dto.AccountCreateRequest;
import com.tuum.banking.model.dto.AccountDto;
import com.tuum.banking.model.dto.TransactionCreateRequest;
import com.tuum.banking.model.dto.TransactionDto;
import com.tuum.banking.service.AccountService;
import com.tuum.banking.service.TransactionService;
import com.tuum.banking.util.Page;
import com.tuum.banking.util.PageRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(API_URI_V1 + "/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(CREATED)
    public AccountDto createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(@PathVariable Long accountId) {
        return accountService.getAccount(accountId)
                .orElseThrow(() -> new BankingException("Account not found by " + accountId));
    }

    @PostMapping("/{accountId}/transaction")
    @ResponseStatus(CREATED)
    public TransactionDto createTransaction(@PathVariable Long accountId, @Valid @RequestBody TransactionCreateRequest request) {
        var account = accountService.getAccount(accountId).orElseThrow(() -> new BankingException("Invalid account"));
        request.setAccountId(account.getAccountId());
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{accountId}/transaction")
    public Page<TransactionDto> readTransactions(@PathVariable Long accountId, PageRequest pageRequest) {
        var account = accountService.getAccount(accountId).orElseThrow(() -> new BankingException("Invalid account"));
        return transactionService.readCollectionByAccountId(account.getAccountId(), pageRequest);
    }

}
