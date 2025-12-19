package com.bank.transfers.fees.domain;

import com.bank.transfers.initiation.domain.FeeType;
import com.bank.transfers.initiation.domain.Money;
import com.bank.transfers.initiation.domain.RailType;
import com.bank.transfers.initiation.domain.TransferFee;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferPricingService {

    private static final BigDecimal INTERNAL_FEE = BigDecimal.ZERO;
    private static final BigDecimal INTERAC_FEE = new BigDecimal("1.50");
    private static final BigDecimal ACH_FEE = new BigDecimal("0.50");
    private static final BigDecimal WIRE_FEE = new BigDecimal("25.00");

    /**
     * Calculates the fee for a transfer based on rail type and amount.
     */
    public TransferFee calculateFee(RailType railType, Money amount) {
        BigDecimal feeAmount;

        switch (railType) {
            case INTERNAL:
                feeAmount = INTERNAL_FEE;
                break;
            case INTERAC:
                feeAmount = INTERAC_FEE;
                break;
            case ACH:
                feeAmount = ACH_FEE;
                break;
            case WIRE:
                feeAmount = WIRE_FEE;
                break;
            default:
                feeAmount = BigDecimal.ZERO;
        }

        if (feeAmount.compareTo(BigDecimal.ZERO) == 0) {
            return TransferFee.zero(amount.getCurrencyCode());
        }

        Money feeMoney = Money.of(feeAmount.setScale(2, RoundingMode.HALF_UP), amount.getCurrencyCode());
        return TransferFee.of(feeMoney, FeeType.FLAT);
    }

    /**
     * Checks if a fee should be applied.
     */
    public boolean shouldApplyFee(RailType railType) {
        return railType != RailType.INTERNAL;
    }
}

