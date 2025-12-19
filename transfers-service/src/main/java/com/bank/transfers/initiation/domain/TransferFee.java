package com.bank.transfers.initiation.domain;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
@Value
public class TransferFee {
    Money amount;
    FeeType type;

    public static TransferFee of(Money amount, FeeType type) {
        return new TransferFee(amount, type);
    }

    public static TransferFee zero(String currencyCode) {
        return new TransferFee(Money.zero(currencyCode), FeeType.NONE);
    }
}

