package com.bank.accounts.lifecycle.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record AccountClosed(
    AccountId accountId,
    Instant closedAt
) {
}

