package com.bank.accounts.posting.domain.events;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.domain.PostingId;
import org.jmolecules.event.annotation.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;

@DomainEvent
public record AccountCredited(
    PostingId postingId,
    AccountId accountId,
    BigDecimal amount,
    String description,
    Instant postedAt
) {
}

