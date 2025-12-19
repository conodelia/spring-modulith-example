package com.bank.transfers.initiation.domain;

import lombok.Value;
import org.jmolecules.ddd.annotation.ValueObject;

@ValueObject
@Value
public class AuthorizationContext {
    AuthorizationMethod method;
    AuthStrength authStrength;
    Integer deviceRiskScore;

    public static AuthorizationContext of(AuthorizationMethod method, AuthStrength authStrength, Integer deviceRiskScore) {
        return new AuthorizationContext(method, authStrength, deviceRiskScore);
    }
}

