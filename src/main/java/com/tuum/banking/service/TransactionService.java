package com.tuum.banking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tuum.banking.model.domain.Transaction;
import com.tuum.banking.model.dto.TransactionCreateRequest;
import com.tuum.banking.model.dto.TransactionDto;
import com.tuum.banking.repository.TransactionRepository;
import com.tuum.banking.service.transaction.TransactionProcessor;
import com.tuum.banking.util.Page;
import com.tuum.banking.util.PageRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionProcessor transactionInProcessor;
    private final TransactionProcessor transactionOutProcessor;
    private final ObjectMapperService objectMapperService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionDto createTransaction(TransactionCreateRequest request) {
        var direction = Transaction.Direction.valueOf(request.getDirection());
        var transactionResult = getProcessor(direction).process(request);
        return objectMapperService.transactionToDto(transactionResult.getTransaction())
                .setBalance(transactionResult.getCurrentBalance());
    }

    @Transactional(readOnly = true)
    public Page<TransactionDto> readCollectionByAccountId(Long accountId, PageRequest pageRequest) {
        List<TransactionDto> transactions = transactionRepository.findAllByAccountId(accountId, pageRequest)
                .stream().map(objectMapperService::transactionToDto)
                .toList();
        Long totalCount = transactionRepository.countByAccountId(accountId);
        return new Page<TransactionDto>()
                .setContent(transactions)
                .setTotalCount(totalCount)
                .setCurrentPage(pageRequest.getPage())
                .setPageSize(pageRequest.getSize());
    }

    protected TransactionProcessor getProcessor(Transaction.Direction direction) {
        return switch (direction) {
            case IN -> transactionInProcessor;
            case OUT -> transactionOutProcessor;
        };
    }

}
