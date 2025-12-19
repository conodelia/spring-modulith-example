package com.bank.transfers.initiation.domain;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
@Value
public class TransferInstruction {
    AccountRef from;
    Destination to; // Can be AccountRef or ExternalDestinationRef
    Money amount;
    RailType rail;
    String memo;

    public static TransferInstruction of(AccountRef from, Destination to, Money amount, RailType rail, String memo) {
        return new TransferInstruction(from, to, amount, rail, memo);
    }
}

