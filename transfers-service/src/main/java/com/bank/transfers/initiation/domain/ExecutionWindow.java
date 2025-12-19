package com.bank.transfers.initiation.domain;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.time.Instant;
import java.time.ZoneId;

@ValueObject
@Value
public class ExecutionWindow {
    Instant requestedExecutionDate;
    String timezone;

    public static ExecutionWindow of(Instant requestedExecutionDate, String timezone) {
        return new ExecutionWindow(requestedExecutionDate, timezone);
    }

    public static ExecutionWindow immediate() {
        return new ExecutionWindow(Instant.now(), ZoneId.systemDefault().getId());
    }
}

