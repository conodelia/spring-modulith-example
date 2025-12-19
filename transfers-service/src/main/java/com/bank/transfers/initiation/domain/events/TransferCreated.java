package com.bank.transfers.initiation.domain.events;

import com.bank.transfers.initiation.domain.*;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record TransferCreated(
    TransferId transferId,
    AccountRef fromAccountId,
    Destination to,
    Money amount,
    RailType railType,
    Instant createdAt
) {
}

