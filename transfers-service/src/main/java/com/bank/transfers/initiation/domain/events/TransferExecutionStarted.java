package com.bank.transfers.initiation.domain.events;

import com.bank.transfers.initiation.domain.TransferId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record TransferExecutionStarted(
    TransferId transferId,
    String holdReference,
    Instant startedAt
) {
}

