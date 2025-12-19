package com.bank.accounts.lifecycle.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.lifecycle.domain.AccountStatus;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record AccountStatusChanged(
    AccountId accountId,
    AccountStatus previousStatus,
    AccountStatus newStatus,
    Instant changedAt
) {
}

