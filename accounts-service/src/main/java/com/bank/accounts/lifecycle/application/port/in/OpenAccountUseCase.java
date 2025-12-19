package com.bank.accounts.lifecycle.application.port.in;

import com.bank.accounts.lifecycle.domain.Account;
import com.bank.accounts.lifecycle.domain.AccountId;

public interface OpenAccountUseCase {
    Account openAccount(OpenAccountCommand command);
    
    record OpenAccountCommand(
        AccountId accountId,
        String customerId,
        String productType
    ) {}
}

