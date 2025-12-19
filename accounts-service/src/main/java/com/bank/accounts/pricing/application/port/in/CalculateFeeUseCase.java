package com.bank.accounts.pricing.application.port.in;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.pricing.domain.Fee;
import com.bank.accounts.posting.domain.PostingType;

import java.math.BigDecimal;

public interface CalculateFeeUseCase {
    Fee assessFee(AssessFeeCommand command);
    
    record AssessFeeCommand(
        AccountId accountId,
        PostingType postingType,
        BigDecimal amount
    ) {}
}

