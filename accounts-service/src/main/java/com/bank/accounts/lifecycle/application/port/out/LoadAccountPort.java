package com.bank.accounts.lifecycle.application.port.out;

import com.bank.accounts.lifecycle.domain.Account;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.util.Optional;

public interface LoadAccountPort {
    Optional<Account> findById(AccountId accountId);
    Account save(Account account);
}

