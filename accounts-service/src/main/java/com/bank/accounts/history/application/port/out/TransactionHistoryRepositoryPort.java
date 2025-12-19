package com.bank.accounts.history.application.port.out;

import com.bank.accounts.history.domain.TransactionEntry;
import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.domain.PostingId;

import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepositoryPort {
    Optional<TransactionEntry> findByPostingId(PostingId postingId);
    List<TransactionEntry> findByAccountId(AccountId accountId);
    TransactionEntry save(TransactionEntry entry);
}

