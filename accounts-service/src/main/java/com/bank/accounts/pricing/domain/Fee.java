package com.bank.accounts.pricing.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fees")
@Getter
public class Fee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    @Embedded
    private AccountId accountId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeType type;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Instant assessedAt;
    
    private boolean waived;
    
    protected Fee() {
        // JPA
    }
    
    public Fee(AccountId accountId, FeeType type, BigDecimal amount, String description) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.assessedAt = Instant.now();
        this.waived = false;
    }
    
    public void waive() {
        this.waived = true;
    }
}

