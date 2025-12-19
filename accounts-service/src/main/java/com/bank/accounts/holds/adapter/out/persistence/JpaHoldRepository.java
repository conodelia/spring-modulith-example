package com.bank.accounts.holds.adapter.out.persistence;

import com.bank.accounts.holds.domain.Hold;
import com.bank.accounts.holds.domain.HoldId;
import com.bank.accounts.holds.domain.HoldStatus;
import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaHoldRepository extends JpaRepository<Hold, HoldId> {
    Optional<Hold> findById(HoldId id);
    
    @Query("SELECT h FROM Hold h WHERE h.accountId = :accountId AND h.status = :status")
    List<Hold> findActiveHoldsByAccountId(@Param("accountId") AccountId accountId, @Param("status") HoldStatus status);
}

@Component
class HoldRepositoryAdapter implements com.bank.accounts.holds.application.port.out.HoldRepositoryPort {
    
    private final JpaHoldRepository repository;
    
    HoldRepositoryAdapter(JpaHoldRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Hold> findById(HoldId holdId) {
        return repository.findById(holdId);
    }
    
    @Override
    public List<Hold> findActiveHoldsByAccountId(AccountId accountId) {
        return repository.findActiveHoldsByAccountId(accountId, HoldStatus.ACTIVE);
    }
    
    @Override
    public Hold save(Hold hold) {
        return repository.save(hold);
    }
}

