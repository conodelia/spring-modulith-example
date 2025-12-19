package com.bank.transfers.initiation.domain.events;

import com.bank.transfers.initiation.domain.FailureReason;
import com.bank.transfers.initiation.domain.TransferId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record TransferFailed(
    TransferId transferId,
    FailureReason failureReason,
    Instant failedAt
) {
}

