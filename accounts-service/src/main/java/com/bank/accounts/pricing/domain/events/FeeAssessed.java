package com.bank.accounts.pricing.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;

@DomainEvent
public record FeeAssessed(
    AccountId accountId,
    BigDecimal amount,
    com.bank.accounts.pricing.domain.FeeType feeType,
    String description,
    Instant assessedAt
) {
}

