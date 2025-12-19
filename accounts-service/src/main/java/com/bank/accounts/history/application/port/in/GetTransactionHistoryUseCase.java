package com.bank.accounts.history.application.port.in;

import com.bank.accounts.history.domain.TransactionEntry;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.util.List;

public interface GetTransactionHistoryUseCase {
    List<TransactionEntry> getTransactionHistory(AccountId accountId);
}

