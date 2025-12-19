package com.bank.accounts.lifecycle.application.service;

import com.bank.accounts.lifecycle.application.port.in.ChangeAccountStatusUseCase;
import com.bank.accounts.lifecycle.application.port.in.CloseAccountUseCase;
import com.bank.accounts.lifecycle.application.port.in.OpenAccountUseCase;
import com.bank.accounts.lifecycle.application.port.out.LoadAccountPort;
import com.bank.accounts.lifecycle.domain.Account;
import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.lifecycle.domain.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountLifecycleService implements OpenAccountUseCase, CloseAccountUseCase, ChangeAccountStatusUseCase {
    
    private final LoadAccountPort loadAccountPort;
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public Account openAccount(OpenAccountCommand command) {
        Account account = Account.open(command.accountId(), command.customerId(), command.productType());
        account = loadAccountPort.save(account);
        publishDomainEvents(account);
        return account;
    }
    
    @Override
    public void closeAccount(AccountId accountId) {
        Account account = loadAccountPort.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        account.close();
        loadAccountPort.save(account);
        publishDomainEvents(account);
    }
    
    @Override
    public void changeStatus(AccountId accountId, AccountStatus newStatus) {
        Account account = loadAccountPort.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
        account.changeStatus(newStatus);
        loadAccountPort.save(account);
        publishDomainEvents(account);
    }
    
    private void publishDomainEvents(Account account) {
        List<Object> events = account.getDomainEvents();
        events.forEach(event -> applicationEventPublisher.publishEvent(event));
        account.clearDomainEvents();
    }
}

