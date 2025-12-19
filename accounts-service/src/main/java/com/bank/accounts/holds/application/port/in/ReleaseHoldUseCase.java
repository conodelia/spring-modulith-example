package com.bank.accounts.holds.application.port.in;

import com.bank.accounts.holds.domain.HoldId;

public interface ReleaseHoldUseCase {
    void releaseHold(HoldId holdId);
}

