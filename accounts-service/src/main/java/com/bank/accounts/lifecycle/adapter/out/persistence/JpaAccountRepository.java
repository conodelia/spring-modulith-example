package com.bank.accounts.lifecycle.adapter.out.persistence;

import com.bank.accounts.lifecycle.domain.Account;
import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, AccountId> {
    Optional<Account> findById(AccountId id);
    List<Account> findAll();
}

@Component
class AccountRepositoryAdapter implements com.bank.accounts.lifecycle.application.port.out.LoadAccountPort, com.bank.accounts.lifecycle.application.port.out.ListAccountsPort {
    
    private final JpaAccountRepository repository;
    
    AccountRepositoryAdapter(JpaAccountRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Account> findById(AccountId accountId) {
        return repository.findById(accountId);
    }
    
    @Override
    public Account save(Account account) {
        return repository.save(account);
    }
    
    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }
}

