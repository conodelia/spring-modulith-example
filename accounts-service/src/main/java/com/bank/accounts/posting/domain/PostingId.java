package com.bank.accounts.posting.domain;

import jakarta.persistence.Embeddable;
import org.jmolecules.ddd.annotation.ValueObject;
import lombok.Value;

import java.util.UUID;

@ValueObject
@Embeddable
@Value
public class PostingId {
    UUID value;
    
    public static PostingId generate() {
        return new PostingId(UUID.randomUUID());
    }
    
    public static PostingId of(String value) {
        return new PostingId(UUID.fromString(value));
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}

