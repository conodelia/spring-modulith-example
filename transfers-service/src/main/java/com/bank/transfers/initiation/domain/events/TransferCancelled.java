package com.bank.transfers.initiation.domain.events;

import com.bank.transfers.initiation.domain.TransferId;
import com.bank.transfers.initiation.domain.TransferStatus;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record TransferCancelled(
    TransferId transferId,
    TransferStatus previousStatus,
    Instant cancelledAt
) {
}

