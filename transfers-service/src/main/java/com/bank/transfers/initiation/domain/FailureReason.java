package com.bank.transfers.initiation.domain;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
@Value
public class FailureReason {
    String code;
    String message;

    public static FailureReason of(String code, String message) {
        return new FailureReason(code, message);
    }
}

