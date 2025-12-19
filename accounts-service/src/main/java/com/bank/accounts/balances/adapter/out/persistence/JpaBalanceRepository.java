package com.bank.accounts.balances.adapter.out.persistence;

import com.bank.accounts.balances.domain.AccountBalance;
import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Repository
public interface JpaBalanceRepository extends JpaRepository<AccountBalance, AccountId> {
    Optional<AccountBalance> findByAccountId(AccountId accountId);
}

@Component
class BalanceRepositoryAdapter implements com.bank.accounts.balances.application.port.out.BalanceRepositoryPort {
    
    private final JpaBalanceRepository repository;
    
    BalanceRepositoryAdapter(JpaBalanceRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<AccountBalance> findByAccountId(AccountId accountId) {
        return repository.findByAccountId(accountId);
    }
    
    @Override
    public AccountBalance save(AccountBalance balance) {
        return repository.save(balance);
    }
}

