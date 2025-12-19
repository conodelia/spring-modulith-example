package com.bank.transfers.beneficiaries.application.port.in;

import com.bank.transfers.beneficiaries.domain.Beneficiary;

public interface AddBeneficiaryUseCase {
    Beneficiary addBeneficiary(AddBeneficiaryCommand command);
    
    record AddBeneficiaryCommand(
        String customerId,
        String tokenizedReference,
        String displayName,
        String institutionName
    ) {}
}

