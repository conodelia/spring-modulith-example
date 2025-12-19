package com.bank.accounts.holds.domain.events;

import com.bank.accounts.holds.domain.HoldId;
import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;

@DomainEvent
public record HoldPlaced(
    HoldId holdId,
    AccountId accountId,
    BigDecimal amount,
    Instant placedAt
) {
}

