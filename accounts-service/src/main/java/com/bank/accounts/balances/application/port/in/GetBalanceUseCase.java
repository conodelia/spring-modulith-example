package com.bank.accounts.balances.application.port.in;

import com.bank.accounts.balances.domain.AccountBalance;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.util.Optional;

public interface GetBalanceUseCase {
    Optional<AccountBalance> getBalance(AccountId accountId);
}

