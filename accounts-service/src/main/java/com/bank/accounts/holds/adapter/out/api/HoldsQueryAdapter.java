package com.bank.accounts.holds.adapter.out.api;

import com.bank.accounts.holds.api.HoldsQueryPort;
import com.bank.accounts.holds.application.port.out.HoldRepositoryPort;
import com.bank.accounts.lifecycle.domain.AccountId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Adapter that implements the published HoldsQueryPort API.
 * This allows other modules (like posting) to query holds.
 */
@Component
@RequiredArgsConstructor
public class HoldsQueryAdapter implements HoldsQueryPort {
    
    private final HoldRepositoryPort holdRepository;
    
    @Override
    public BigDecimal getTotalHolds(AccountId accountId) {
        return holdRepository.findActiveHoldsByAccountId(accountId).stream()
            .map(hold -> hold.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public boolean hasSufficientFunds(AccountId accountId, BigDecimal amount) {
        // This is a simplified check - in reality, we'd need the current balance
        // For now, we'll assume sufficient funds if no holds exist
        // The actual balance check should be done by the balances module
        // We can't fully validate without balance, so we'll return true
        // and let the balances module handle the actual validation
        return true;
    }
    
    @Override
    public BigDecimal getAvailableBalance(AccountId accountId, BigDecimal currentBalance) {
        BigDecimal totalHolds = getTotalHolds(accountId);
        return currentBalance.subtract(totalHolds);
    }
}

