package com.bank.accounts.history.adapter.out.persistence;

import com.bank.accounts.history.domain.TransactionEntry;
import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.domain.PostingId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTransactionHistoryRepository extends JpaRepository<TransactionEntry, String> {
    @Query("SELECT t FROM TransactionEntry t WHERE t.postingId = :postingId")
    Optional<TransactionEntry> findByPostingId(@Param("postingId") PostingId postingId);
    
    @Query("SELECT t FROM TransactionEntry t WHERE t.accountId = :accountId ORDER BY t.postedAt DESC")
    List<TransactionEntry> findByAccountId(@Param("accountId") AccountId accountId);
}

@Component
class TransactionHistoryRepositoryAdapter implements com.bank.accounts.history.application.port.out.TransactionHistoryRepositoryPort {
    
    private final JpaTransactionHistoryRepository repository;
    
    TransactionHistoryRepositoryAdapter(JpaTransactionHistoryRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<TransactionEntry> findByPostingId(PostingId postingId) {
        return repository.findByPostingId(postingId);
    }
    
    @Override
    public List<TransactionEntry> findByAccountId(AccountId accountId) {
        return repository.findByAccountId(accountId);
    }
    
    @Override
    public TransactionEntry save(TransactionEntry entry) {
        return repository.save(entry);
    }
}

