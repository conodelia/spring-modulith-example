package com.bank.transfers.initiation.adapter.out.http;

import com.bank.transfers.initiation.application.port.out.RiskPort;
import com.bank.transfers.initiation.domain.Money;
import com.bank.transfers.initiation.domain.TransferId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * HTTP adapter for RiskPort.
 * In a real system, this would call a Risk/Fraud service.
 */
@Component
@RequiredArgsConstructor
public class RiskPortHttpAdapter implements RiskPort {

    @Override
    public Optional<RiskAssessment> assessTransferRisk(TransferId transferId, Money amount, String fromAccountId, String toDestination) {
        // Placeholder implementation
        // In a real system, this would call Risk service REST API
        // For demo, approve all transfers
        return Optional.of(new RiskAssessment(true, "Approved", 10));
    }
}

