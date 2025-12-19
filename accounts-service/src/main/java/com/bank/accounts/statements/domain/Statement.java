package com.bank.accounts.statements.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;

@Entity
@Table(name = "statements")
@Getter
public class Statement {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "statement_id"))
    private StatementId id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId accountId;
    
    @Column(nullable = false)
    private YearMonth period;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal openingBalance;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal closingBalance;
    
    @Column(nullable = false)
    private Instant generatedAt;
    
    private Instant readyAt;
    
    protected Statement() {
        // JPA
    }
    
    public Statement(StatementId id, AccountId accountId, YearMonth period, 
                    BigDecimal openingBalance, BigDecimal closingBalance) {
        this.id = id;
        this.accountId = accountId;
        this.period = period;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.generatedAt = Instant.now();
    }
    
    public void markAsReady() {
        this.readyAt = Instant.now();
    }
}

