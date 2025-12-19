package com.bank.accounts.lifecycle.application.port.in;

import com.bank.accounts.lifecycle.domain.Account;

import java.util.List;

public interface GetAllAccountsUseCase {
    List<Account> getAllAccounts();
}

