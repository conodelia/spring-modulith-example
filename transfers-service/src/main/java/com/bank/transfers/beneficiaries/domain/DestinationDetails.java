package com.bank.transfers.beneficiaries.domain;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;

@ValueObject
@Value
@Embeddable
public class DestinationDetails implements Serializable {
    String tokenizedReference; // Never raw account numbers
    String displayName; // For UI display only
    String institutionName; // Bank name or institution

    public static DestinationDetails of(String tokenizedReference, String displayName, String institutionName) {
        if (tokenizedReference == null || tokenizedReference.trim().isEmpty()) {
            throw new IllegalArgumentException("Tokenized reference cannot be null or empty");
        }
        return new DestinationDetails(tokenizedReference, displayName, institutionName);
    }
}

