package com.bank.transfers.initiation.adapter.out.persistence;

import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTransferRepository extends JpaRepository<Transfer, TransferId> {
    Optional<Transfer> findById(TransferId id);
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);
    List<Transfer> findByCustomerId(String customerId);
}

@Component
class TransferRepositoryAdapter implements com.bank.transfers.initiation.application.port.out.TransferRepositoryPort {
    
    private final JpaTransferRepository repository;
    
    TransferRepositoryAdapter(JpaTransferRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Transfer> findById(TransferId transferId) {
        return repository.findById(transferId);
    }
    
    @Override
    public Optional<Transfer> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey);
    }
    
    @Override
    public Transfer save(Transfer transfer) {
        return repository.save(transfer);
    }
    
    @Override
    public List<Transfer> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }
    
    @Override
    public List<Transfer> findAll() {
        return repository.findAll();
    }
}

