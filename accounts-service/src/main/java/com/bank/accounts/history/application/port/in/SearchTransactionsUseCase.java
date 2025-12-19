package com.bank.accounts.history.application.port.in;

import com.bank.accounts.history.domain.TransactionEntry;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.time.Instant;
import java.util.List;

public interface SearchTransactionsUseCase {
    List<TransactionEntry> searchTransactions(SearchCriteria criteria);
    
    record SearchCriteria(
        AccountId accountId,
        Instant fromDate,
        Instant toDate,
        com.bank.accounts.posting.domain.PostingType type,
        com.bank.accounts.posting.domain.PostingStatus status
    ) {}
}

