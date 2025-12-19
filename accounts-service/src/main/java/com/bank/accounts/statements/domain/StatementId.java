package com.bank.accounts.statements.domain;

import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.annotation.ValueObject;
import lombok.Value;

import java.util.UUID;

@ValueObject
@Embeddable
@Value
public class StatementId {
    UUID value;
    
    public static StatementId generate() {
        return new StatementId(UUID.randomUUID());
    }
    
    public static StatementId of(String value) {
        return new StatementId(UUID.fromString(value));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

