package com.bank.transfers.initiation.domain;

import jakarta.persistence.Embeddable;
import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

import java.io.Serializable;
import java.util.UUID;

@ValueObject
@Value
@Embeddable
public class AccountRef implements Serializable {
    UUID accountId;

    public static AccountRef of(UUID accountId) {
        return new AccountRef(accountId);
    }

    public static AccountRef of(String accountId) {
        return new AccountRef(UUID.fromString(accountId));
    }

    @Override
    public String toString() {
        return accountId.toString();
    }
}

