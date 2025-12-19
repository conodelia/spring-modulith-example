package com.bank.accounts.statements.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.statements.domain.StatementId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record StatementGenerated(
    StatementId statementId,
    AccountId accountId,
    com.bank.accounts.statements.domain.StatementPeriod period,
    Instant generatedAt
) {
}

