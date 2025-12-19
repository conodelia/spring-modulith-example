package com.bank.accounts.holds.domain.events;

import com.bank.accounts.holds.domain.HoldId;
import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record HoldReleased(
    HoldId holdId,
    AccountId accountId,
    Instant releasedAt
) {
}

