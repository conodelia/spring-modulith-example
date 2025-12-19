package com.bank.accounts.pricing.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "interest_accruals")
@Getter
public class InterestAccrual {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    @Embedded
    private AccountId accountId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterestType type;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private Instant accruedAt;
    
    protected InterestAccrual() {
        // JPA
    }
    
    public InterestAccrual(AccountId accountId, InterestType type, BigDecimal amount) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.accruedAt = Instant.now();
    }
}

