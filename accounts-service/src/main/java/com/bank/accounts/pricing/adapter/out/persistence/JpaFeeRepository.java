package com.bank.accounts.pricing.adapter.out.persistence;

import com.bank.accounts.pricing.domain.Fee;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Repository
public interface JpaFeeRepository extends JpaRepository<Fee, String> {
}

@Component
class FeeRepositoryAdapter implements com.bank.accounts.pricing.application.port.out.FeeRepositoryPort {
    
    private final JpaFeeRepository repository;
    
    FeeRepositoryAdapter(JpaFeeRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Fee save(Fee fee) {
        return repository.save(fee);
    }
}

