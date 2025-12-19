package com.bank.accounts.holds.application.port.out;

import com.bank.accounts.holds.domain.Hold;
import com.bank.accounts.holds.domain.HoldId;
import com.bank.accounts.lifecycle.domain.AccountId;

import java.util.List;
import java.util.Optional;

public interface HoldRepositoryPort {
    Optional<Hold> findById(HoldId holdId);
    List<Hold> findActiveHoldsByAccountId(AccountId accountId);
    Hold save(Hold hold);
}

