package com.bank.accounts.balances.application.port.out;

import com.bank.accounts.balances.domain.AccountBalance;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.util.Optional;

public interface BalanceRepositoryPort {
    Optional<AccountBalance> findByAccountId(AccountId accountId);
    AccountBalance save(AccountBalance balance);
}

