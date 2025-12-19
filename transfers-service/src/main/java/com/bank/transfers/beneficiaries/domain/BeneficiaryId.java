package com.bank.transfers.beneficiaries.domain;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;
import java.util.UUID;

@ValueObject
@Value
@Embeddable
public class BeneficiaryId implements Serializable {
    UUID value;

    public static BeneficiaryId generate() {
        return new BeneficiaryId(UUID.randomUUID());
    }

    public static BeneficiaryId of(String value) {
        return new BeneficiaryId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

