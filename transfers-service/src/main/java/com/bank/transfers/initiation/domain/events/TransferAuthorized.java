package com.bank.transfers.initiation.domain.events;

import com.bank.transfers.initiation.domain.AuthorizationContext;
import com.bank.transfers.initiation.domain.TransferId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record TransferAuthorized(
    TransferId transferId,
    AuthorizationContext authorizationContext,
    Instant authorizedAt
) {
}

