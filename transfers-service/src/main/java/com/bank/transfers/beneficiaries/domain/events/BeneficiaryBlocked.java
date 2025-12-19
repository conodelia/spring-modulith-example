package com.bank.transfers.beneficiaries.domain.events;

import com.bank.transfers.beneficiaries.domain.BeneficiaryId;
import com.bank.transfers.beneficiaries.domain.BeneficiaryStatus;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record BeneficiaryBlocked(
    BeneficiaryId beneficiaryId,
    BeneficiaryStatus previousStatus,
    Instant blockedAt
) {
}

