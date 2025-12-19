package com.bank.accounts.pricing.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;

@DomainEvent
public record InterestAccrued(
    AccountId accountId,
    BigDecimal amount,
    com.bank.accounts.pricing.domain.InterestType interestType,
    Instant accruedAt
) {
}

