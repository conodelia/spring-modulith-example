package com.bank.accounts.pricing.adapter.out.persistence;

import com.bank.accounts.pricing.domain.InterestAccrual;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Repository
public interface JpaInterestRepository extends JpaRepository<InterestAccrual, String> {
}

@Component
class InterestRepositoryAdapter implements com.bank.accounts.pricing.application.port.out.InterestRepositoryPort {
    
    private final JpaInterestRepository repository;
    
    InterestRepositoryAdapter(JpaInterestRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public InterestAccrual save(InterestAccrual accrual) {
        return repository.save(accrual);
    }
}

