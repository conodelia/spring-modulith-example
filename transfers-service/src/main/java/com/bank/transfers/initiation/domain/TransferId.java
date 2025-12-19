package com.bank.transfers.initiation.domain;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;
import java.util.UUID;

@ValueObject
@Value
@Embeddable
public class TransferId implements Serializable {
    UUID value;

    public static TransferId generate() {
        return new TransferId(UUID.randomUUID());
    }

    public static TransferId of(String value) {
        return new TransferId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

