package com.bank.accounts.statements.domain.events;

import com.bank.accounts.statements.domain.StatementId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record StatementReady(
    StatementId statementId,
    Instant readyAt
) {
}

