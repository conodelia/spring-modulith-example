package com.bank.accounts.lifecycle.application.service;

import com.bank.accounts.lifecycle.application.port.in.GetAllAccountsUseCase;
import com.bank.accounts.lifecycle.application.port.out.ListAccountsPort;
import com.bank.accounts.lifecycle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryService implements GetAllAccountsUseCase {
    
    private final ListAccountsPort listAccountsPort;
    
    @Override
    public List<Account> getAllAccounts() {
        return listAccountsPort.findAll();
    }
}

