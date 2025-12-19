package com.bank.transfers.beneficiaries.application.port.out;

import com.bank.transfers.beneficiaries.domain.Beneficiary;
import com.bank.transfers.beneficiaries.domain.BeneficiaryId;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepositoryPort {
    Optional<Beneficiary> findById(BeneficiaryId beneficiaryId);
    Beneficiary save(Beneficiary beneficiary);
    List<Beneficiary> findByCustomerId(String customerId);
}

