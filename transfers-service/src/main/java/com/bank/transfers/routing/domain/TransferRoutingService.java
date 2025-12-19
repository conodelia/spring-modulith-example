package com.bank.transfers.routing.domain;

import com.bank.transfers.initiation.domain.Destination;
import com.bank.transfers.initiation.domain.Money;
import com.bank.transfers.initiation.domain.RailType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class TransferRoutingService {

    private static final LocalTime CUTOFF_TIME = LocalTime.of(15, 0); // 3 PM cutoff

    /**
     * Decides the rail type based on destination, currency, amount, and cutoff times.
     */
    public RailType determineRail(Destination destination, Money amount, String currency, Instant requestedExecutionDate) {
        // If internal (same bank)
        if (destination.isInternal()) {
            return RailType.INTERNAL;
        }

        // If external, determine based on amount and currency
        // For demo purposes, simple rules:
        // - Small amounts (< $1000) -> INTERAC
        // - Medium amounts ($1000-$10000) -> ACH
        // - Large amounts (> $10000) -> WIRE

        BigDecimal amountValue = amount.getAmount();
        if (amountValue.compareTo(new java.math.BigDecimal("1000")) < 0) {
            return RailType.INTERAC;
        } else if (amountValue.compareTo(new java.math.BigDecimal("10000")) <= 0) {
            return RailType.ACH;
        } else {
            return RailType.WIRE;
        }
    }

    /**
     * Checks if the requested execution date is within cutoff window.
     */
    public boolean isWithinCutoffWindow(Instant requestedExecutionDate, String timezone) {
        if (requestedExecutionDate == null) {
            return true; // Immediate execution
        }

        LocalTime requestedTime = requestedExecutionDate
            .atZone(ZoneId.of(timezone))
            .toLocalTime();

        return requestedTime.isBefore(CUTOFF_TIME);
    }

    /**
     * Determines if transfer should be executed immediately or scheduled.
     */
    public boolean shouldExecuteImmediately(Instant requestedExecutionDate) {
        if (requestedExecutionDate == null) {
            return true;
        }
        return requestedExecutionDate.isBefore(Instant.now()) || 
               requestedExecutionDate.equals(Instant.now());
    }
}

