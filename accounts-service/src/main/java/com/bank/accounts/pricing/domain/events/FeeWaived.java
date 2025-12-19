package com.bank.accounts.pricing.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record FeeWaived(
    AccountId accountId,
    com.bank.accounts.pricing.domain.FeeType feeType,
    String reason,
    Instant waivedAt
) {
}

