package com.bank.accounts.balances.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;

@DomainEvent
public record BalanceUpdated(
    AccountId accountId,
    BigDecimal currentBalance,
    BigDecimal availableBalance,
    Instant updatedAt
) {
}

