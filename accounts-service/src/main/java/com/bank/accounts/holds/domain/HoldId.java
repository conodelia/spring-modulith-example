package com.bank.accounts.holds.domain;

import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.annotation.ValueObject;
import lombok.Value;

import java.util.UUID;

@ValueObject
@Embeddable
@Value
public class HoldId {
    UUID value;
    
    public static HoldId generate() {
        return new HoldId(UUID.randomUUID());
    }
    
    public static HoldId of(String value) {
        return new HoldId(UUID.fromString(value));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

