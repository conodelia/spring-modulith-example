package com.bank.transfers.initiation.domain;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;

@ValueObject
@Value
@Embeddable
public class Destination implements Serializable {
    AccountRef accountRef;
    ExternalDestinationRef externalRef;

    public static Destination internal(AccountRef accountRef) {
        return new Destination(accountRef, null);
    }

    public static Destination external(ExternalDestinationRef externalRef) {
        return new Destination(null, externalRef);
    }

    public boolean isInternal() {
        return accountRef != null;
    }

    public boolean isExternal() {
        return externalRef != null;
    }
}

