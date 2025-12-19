package com.bank.accounts.holds.application.port.in;

import com.bank.accounts.holds.domain.Hold;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.math.BigDecimal;
import java.time.Instant;

public interface PlaceHoldUseCase {
    Hold placeHold(PlaceHoldCommand command);
    
    record PlaceHoldCommand(
        AccountId accountId,
        com.bank.accounts.holds.domain.HoldType type,
        BigDecimal amount,
        String reason,
        Instant expiresAt
    ) {}
}

