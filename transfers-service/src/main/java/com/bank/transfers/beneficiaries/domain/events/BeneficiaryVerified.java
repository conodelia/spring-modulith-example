package com.bank.transfers.beneficiaries.domain.events;

import com.bank.transfers.beneficiaries.domain.BeneficiaryId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record BeneficiaryVerified(
    BeneficiaryId beneficiaryId,
    Instant verifiedAt
) {
}

