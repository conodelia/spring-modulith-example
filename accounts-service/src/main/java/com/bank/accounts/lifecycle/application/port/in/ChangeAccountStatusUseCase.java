package com.bank.accounts.lifecycle.application.port.in;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.lifecycle.domain.AccountStatus;

public interface ChangeAccountStatusUseCase {
    void changeStatus(AccountId accountId, AccountStatus newStatus);
}

