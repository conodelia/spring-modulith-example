package com.bank.transfers.initiation.application.port.out;

import com.bank.transfers.initiation.domain.AccountRef;

import java.util.Optional;

/**
 * Outbound port for communicating with Identity/Entitlements service.
 */
public interface IdentityPort {
    
    /**
     * Verify customer is entitled to use the account.
     * @param customerId Customer ID
     * @param accountRef Account reference
     * @return true if entitled
     */
    boolean verifyCustomerEntitled(String customerId, AccountRef accountRef);
    
    /**
     * Check if step-up authentication is required.
     * @param customerId Customer ID
     * @param accountRef Account reference
     * @param amount Transfer amount
     * @return true if step-up required
     */
    boolean stepUpRequired(String customerId, AccountRef accountRef, String amount);
}

