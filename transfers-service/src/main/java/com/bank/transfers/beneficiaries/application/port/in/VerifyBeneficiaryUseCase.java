package com.bank.transfers.beneficiaries.application.port.in;

import com.bank.transfers.beneficiaries.domain.Beneficiary;
import com.bank.transfers.beneficiaries.domain.BeneficiaryId;

public interface VerifyBeneficiaryUseCase {
    Beneficiary verifyBeneficiary(BeneficiaryId beneficiaryId);
}

