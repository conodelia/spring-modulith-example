package com.bank.transfers.initiation.adapter.out.http;

import com.bank.transfers.initiation.application.port.out.IdentityPort;
import com.bank.transfers.initiation.domain.AccountRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * HTTP adapter for IdentityPort.
 * In a real system, this would call an Identity/Entitlements service.
 */
@Component
@RequiredArgsConstructor
public class IdentityPortHttpAdapter implements IdentityPort {

    @Override
    public boolean verifyCustomerEntitled(String customerId, AccountRef accountRef) {
        // Placeholder implementation
        // In a real system, this would call Identity service REST API
        // For demo, always return true
        return true;
    }

    @Override
    public boolean stepUpRequired(String customerId, AccountRef accountRef, String amount) {
        // Placeholder implementation
        // In a real system, this would call Identity service REST API
        // For demo, return false
        return false;
    }
}

