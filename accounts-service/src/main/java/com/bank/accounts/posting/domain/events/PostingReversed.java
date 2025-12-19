package com.bank.accounts.posting.domain.events;

import com.bank.accounts.posting.domain.PostingId;
import org.jmolecules.event.annotation.DomainEvent;

import java.time.Instant;

@DomainEvent
public record PostingReversed(
    PostingId postingId,
    PostingId reversalPostingId,
    Instant reversedAt
) {
}

