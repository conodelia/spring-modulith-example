package com.bank.transfers.beneficiaries.domain.events;

import com.bank.transfers.beneficiaries.domain.BeneficiaryId;
import com.bank.transfers.beneficiaries.domain.DestinationDetails;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record BeneficiaryEnrolled(
    BeneficiaryId beneficiaryId,
    String customerId,
    DestinationDetails destinationDetails,
    Instant enrolledAt
) {
}

