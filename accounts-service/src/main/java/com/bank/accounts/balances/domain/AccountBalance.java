package com.bank.accounts.balances.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "account_balances")
@Getter
public class AccountBalance {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId accountId;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentBalance;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal availableBalance;
    
    @Column(nullable = false)
    private Instant lastUpdated;
    
    @Version
    private Long version;
    
    protected AccountBalance() {
        // JPA
    }
    
    public AccountBalance(AccountId accountId) {
        this.accountId = accountId;
        this.currentBalance = BigDecimal.ZERO;
        this.availableBalance = BigDecimal.ZERO;
        this.lastUpdated = Instant.now();
    }
    
    public void updateCurrentBalance(BigDecimal newBalance) {
        this.currentBalance = newBalance;
        this.lastUpdated = Instant.now();
    }
    
    public void updateAvailableBalance(BigDecimal newBalance) {
        this.availableBalance = newBalance;
        this.lastUpdated = Instant.now();
    }
    
    public void applyDebit(BigDecimal amount) {
        this.currentBalance = this.currentBalance.subtract(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
        this.lastUpdated = Instant.now();
    }
    
    public void applyCredit(BigDecimal amount) {
        this.currentBalance = this.currentBalance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
        this.lastUpdated = Instant.now();
    }
}

