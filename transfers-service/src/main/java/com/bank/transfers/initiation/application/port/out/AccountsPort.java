package com.bank.transfers.initiation.application.port.out;

import com.bank.transfers.initiation.domain.AccountRef;
import com.bank.transfers.initiation.domain.Money;

import java.util.Optional;

/**
 * Outbound port for communicating with Accounts service.
 * This port is implemented by an HTTP client adapter (no direct dependency on accounts-service).
 */
public interface AccountsPort {
    
    /**
     * Place a hold on an account.
     * @param accountRef Account reference
     * @param amount Amount to hold
     * @param reference Reference for the hold
     * @return Hold reference if successful
     */
    Optional<String> placeHold(AccountRef accountRef, Money amount, String reference);
    
    /**
     * Release a hold on an account.
     * @param holdReference Hold reference to release
     * @return true if successful
     */
    boolean releaseHold(String holdReference);
    
    /**
     * Post a debit to an account.
     * @param accountRef Account reference
     * @param amount Amount to debit
     * @param reference Transaction reference
     * @return true if successful
     */
    boolean postDebit(AccountRef accountRef, Money amount, String reference);
    
    /**
     * Post a credit to an account (for internal transfers).
     * @param accountRef Account reference
     * @param amount Amount to credit
     * @param reference Transaction reference
     * @return true if successful
     */
    boolean postCredit(AccountRef accountRef, Money amount, String reference);
}

