package com.bank.accounts.holds.api;

import com.bank.accounts.lifecycle.domain.AccountId;

import java.math.BigDecimal;

/**
 * Published API for querying holds on accounts.
 * This interface is in the api package to allow other modules to depend on it.
 */
public interface HoldsQueryPort {
    /**
     * Get the total amount of holds on an account.
     */
    BigDecimal getTotalHolds(AccountId accountId);
    
    /**
     * Check if an account has sufficient available funds (balance - holds).
     */
    boolean hasSufficientFunds(AccountId accountId, BigDecimal amount);
    
    /**
     * Get available balance (current balance - holds).
     */
    BigDecimal getAvailableBalance(AccountId accountId, BigDecimal currentBalance);
}

