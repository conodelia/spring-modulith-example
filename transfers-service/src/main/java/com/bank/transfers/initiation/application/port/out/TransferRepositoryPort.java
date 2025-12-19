package com.bank.transfers.initiation.application.port.out;

import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;

import java.util.List;
import java.util.Optional;

public interface TransferRepositoryPort {
    Optional<Transfer> findById(TransferId transferId);
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);
    Transfer save(Transfer transfer);
    List<Transfer> findByCustomerId(String customerId);
    List<Transfer> findAll();
}

