package com.bank.accounts.lifecycle.application.port.in;

import com.bank.accounts.lifecycle.domain.AccountId;

public interface CloseAccountUseCase {
    void closeAccount(AccountId accountId);
}

