package com.bank.transfers.initiation.domain;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;

@ValueObject
@Value
@Embeddable
public class ExternalDestinationRef implements Serializable {
    String token; // Tokenized reference, never raw account numbers

    public static ExternalDestinationRef of(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        return new ExternalDestinationRef(token);
    }
}

