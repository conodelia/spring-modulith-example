package com.bank.accounts.posting.application.port.in;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.domain.Posting;
import com.bank.accounts.posting.domain.PostingType;

import java.math.BigDecimal;

public interface PostTransactionUseCase {
    Posting postTransaction(PostTransactionCommand command);
    
    record PostTransactionCommand(
        AccountId accountId,
        PostingType type,
        BigDecimal amount,
        String description,
        String transactionReference
    ) {}
}

