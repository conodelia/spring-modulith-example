package com.bank.accounts.holds.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "holds")
@Getter
public class Hold {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "hold_id"))
    private HoldId id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId accountId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HoldType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HoldStatus status;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String reason;
    
    @Column(nullable = false)
    private Instant placedAt;
    
    private Instant releasedAt;
    
    private Instant expiresAt;
    
    protected Hold() {
        // JPA
    }
    
    private Hold(HoldId id, AccountId accountId, HoldType type, BigDecimal amount, 
                String reason, Instant expiresAt) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.reason = reason;
        this.status = HoldStatus.ACTIVE;
        this.placedAt = Instant.now();
        this.expiresAt = expiresAt;
    }
    
    public static Hold place(HoldId id, AccountId accountId, HoldType type, 
                            BigDecimal amount, String reason, Instant expiresAt) {
        return new Hold(id, accountId, type, amount, reason, expiresAt);
    }
    
    public void release() {
        if (this.status != HoldStatus.ACTIVE) {
            throw new IllegalStateException("Only active holds can be released");
        }
        this.status = HoldStatus.RELEASED;
        this.releasedAt = Instant.now();
    }
    
    public void expire() {
        if (this.status == HoldStatus.ACTIVE) {
            this.status = HoldStatus.EXPIRED;
        }
    }
    
    public boolean isActive() {
        return status == HoldStatus.ACTIVE;
    }
}

