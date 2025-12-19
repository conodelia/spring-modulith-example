package com.bank.transfers.beneficiaries.adapter.out.persistence;

import com.bank.transfers.beneficiaries.domain.Beneficiary;
import com.bank.transfers.beneficiaries.domain.BeneficiaryId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaBeneficiaryRepository extends JpaRepository<Beneficiary, BeneficiaryId> {
    Optional<Beneficiary> findById(BeneficiaryId id);
    List<Beneficiary> findByCustomerId(String customerId);
}

@Component
class BeneficiaryRepositoryAdapter implements com.bank.transfers.beneficiaries.application.port.out.BeneficiaryRepositoryPort {
    
    private final JpaBeneficiaryRepository repository;
    
    BeneficiaryRepositoryAdapter(JpaBeneficiaryRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Beneficiary> findById(BeneficiaryId beneficiaryId) {
        return repository.findById(beneficiaryId);
    }
    
    @Override
    public Beneficiary save(Beneficiary beneficiary) {
        return repository.save(beneficiary);
    }
    
    @Override
    public List<Beneficiary> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }
}

