package com.bank.accounts.holds.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record AccountFrozen(
    AccountId accountId,
    String reason,
    Instant frozenAt
) {
}

