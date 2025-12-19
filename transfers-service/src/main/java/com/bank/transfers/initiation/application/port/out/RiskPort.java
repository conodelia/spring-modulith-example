package com.bank.transfers.initiation.application.port.out;

import com.bank.transfers.initiation.domain.Money;
import com.bank.transfers.initiation.domain.TransferId;

import java.util.Optional;

/**
 * Outbound port for communicating with Risk/Fraud service.
 */
public interface RiskPort {
    
    /**
     * Assess transfer risk.
     * @param transferId Transfer ID
     * @param amount Transfer amount
     * @param fromAccountId From account ID
     * @param toDestination To destination
     * @return Risk assessment result
     */
    Optional<RiskAssessment> assessTransferRisk(TransferId transferId, Money amount, String fromAccountId, String toDestination);
    
    record RiskAssessment(
        boolean approved,
        String reason,
        Integer riskScore
    ) {}
}

