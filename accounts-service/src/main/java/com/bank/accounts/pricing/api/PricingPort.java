package com.bank.accounts.pricing.api;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.domain.PostingType;

import java.math.BigDecimal;

/**
 * Published API for pricing calculations (fees and interest).
 * This interface is in the api package to allow other modules to depend on it.
 */
public interface PricingPort {
    /**
     * Calculate fee for a transaction.
     */
    BigDecimal calculateFee(AccountId accountId, PostingType type, BigDecimal amount);
    
    /**
     * Check if fee should be applied.
     */
    boolean shouldApplyFee(AccountId accountId, PostingType type, BigDecimal amount);
}

