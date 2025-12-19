package com.bank.accounts.lifecycle.application.port.out;

import com.bank.accounts.lifecycle.domain.Account;

import java.util.List;

public interface ListAccountsPort {
    List<Account> findAll();
}

