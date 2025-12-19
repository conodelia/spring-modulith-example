package com.bank.transfers.limitspolicy.domain;

import com.bank.transfers.initiation.domain.AuthorizationContext;
import com.bank.transfers.initiation.domain.AuthStrength;
import com.bank.transfers.initiation.domain.Money;
import org.springframework.stereotype.Service;

@Service
public class TransferPolicyService {

    private static final Money STEP_UP_THRESHOLD = Money.of(
        new java.math.BigDecimal("5000"),
        "USD"
    );
    private static final Integer HIGH_RISK_DEVICE_SCORE = 70;

    /**
     * Evaluates if step-up authentication is required.
     */
    public boolean requiresStepUp(Money transferAmount, AuthorizationContext authContext, String channel) {
        // Require step-up if:
        // 1. Amount exceeds threshold
        // 2. Auth strength is weak
        // 3. Device risk score is high

        if (transferAmount.isGreaterThan(STEP_UP_THRESHOLD)) {
            return true;
        }

        if (authContext != null && authContext.getAuthStrength() == AuthStrength.WEAK) {
            return true;
        }

        if (authContext != null && authContext.getDeviceRiskScore() != null &&
            authContext.getDeviceRiskScore() > HIGH_RISK_DEVICE_SCORE) {
            return true;
        }

        return false;
    }

    /**
     * Evaluates if transfer should be blocked based on policy rules.
     */
    public boolean shouldBlock(Money transferAmount, String customerId, String channel) {
        // In a real system, this would check:
        // - Sanctions lists
        // - Compliance rules
        // - Risk flags
        // For demo, simple rule: block if amount > $100,000
        return transferAmount.isGreaterThan(Money.of(new java.math.BigDecimal("100000"), "USD"));
    }

    /**
     * Checks velocity limits (number of transfers in a time period).
     */
    public boolean checkVelocity(String customerId, String period) {
        // In a real system, this would query velocity counters
        // For demo, always return true
        return true;
    }
}

