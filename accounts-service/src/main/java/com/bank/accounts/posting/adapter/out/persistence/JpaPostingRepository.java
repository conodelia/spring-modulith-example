package com.bank.accounts.posting.adapter.out.persistence;

import com.bank.accounts.posting.domain.Posting;
import com.bank.accounts.posting.domain.PostingId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Repository
public interface JpaPostingRepository extends JpaRepository<Posting, PostingId> {
    Optional<Posting> findById(PostingId id);
    Optional<Posting> findByTransactionReference(String transactionReference);
}

@Component
class PostingRepositoryAdapter implements com.bank.accounts.posting.application.port.out.PostingRepositoryPort {
    
    private final JpaPostingRepository repository;
    
    PostingRepositoryAdapter(JpaPostingRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Posting> findById(PostingId postingId) {
        return repository.findById(postingId);
    }
    
    @Override
    public Optional<Posting> findByTransactionReference(String transactionReference) {
        return repository.findByTransactionReference(transactionReference);
    }
    
    @Override
    public Posting save(Posting posting) {
        return repository.save(posting);
    }
}

@Component
class AccountLoaderAdapter implements com.bank.accounts.posting.application.port.out.LoadAccountPort {
    
    private final com.bank.accounts.lifecycle.adapter.out.persistence.JpaAccountRepository accountRepository;
    
    AccountLoaderAdapter(com.bank.accounts.lifecycle.adapter.out.persistence.JpaAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Override
    public Optional<com.bank.accounts.lifecycle.domain.Account> findById(com.bank.accounts.lifecycle.domain.AccountId accountId) {
        return accountRepository.findById(accountId);
    }
}

