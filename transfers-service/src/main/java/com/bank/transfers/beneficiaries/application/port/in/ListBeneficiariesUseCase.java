package com.bank.transfers.beneficiaries.application.port.in;

import com.bank.transfers.beneficiaries.domain.Beneficiary;

import java.util.List;

public interface ListBeneficiariesUseCase {
    List<Beneficiary> listBeneficiaries(String customerId);
}

