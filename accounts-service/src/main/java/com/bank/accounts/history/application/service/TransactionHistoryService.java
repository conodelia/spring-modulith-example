package com.bank.accounts.history.application.service;

import com.bank.accounts.history.application.port.in.GetTransactionHistoryUseCase;
import com.bank.accounts.history.application.port.in.SearchTransactionsUseCase;
import com.bank.accounts.history.application.port.out.TransactionHistoryRepositoryPort;
import com.bank.accounts.history.domain.TransactionEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionHistoryService implements GetTransactionHistoryUseCase, SearchTransactionsUseCase {
    
    private final TransactionHistoryRepositoryPort repository;
    
    @Override
    public List<TransactionEntry> getTransactionHistory(com.bank.accounts.lifecycle.domain.AccountId accountId) {
        return repository.findByAccountId(accountId);
    }
    
    @Override
    public List<TransactionEntry> searchTransactions(SearchCriteria criteria) {
        List<TransactionEntry> entries = repository.findByAccountId(criteria.accountId());
        
        return entries.stream()
            .filter(entry -> criteria.fromDate() == null || !entry.getPostedAt().isBefore(criteria.fromDate()))
            .filter(entry -> criteria.toDate() == null || !entry.getPostedAt().isAfter(criteria.toDate()))
            .filter(entry -> criteria.type() == null || entry.getType() == criteria.type())
            .filter(entry -> criteria.status() == null || entry.getStatus() == criteria.status())
            .collect(Collectors.toList());
    }
}

