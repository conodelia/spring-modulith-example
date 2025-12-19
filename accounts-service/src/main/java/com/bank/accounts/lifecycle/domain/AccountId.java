package com.bank.accounts.lifecycle.domain;

import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.annotation.ValueObject;
import lombok.Value;

import java.util.UUID;

@ValueObject
@Embeddable
@Value
public class AccountId {
    UUID value;
    
    public static AccountId generate() {
        return new AccountId(UUID.randomUUID());
    }
    
    public static AccountId of(String value) {
        return new AccountId(UUID.fromString(value));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

