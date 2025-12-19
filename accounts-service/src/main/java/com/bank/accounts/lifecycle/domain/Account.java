package com.bank.accounts.lifecycle.domain;

import com.bank.accounts.lifecycle.domain.events.AccountClosed;
import com.bank.accounts.lifecycle.domain.events.AccountOpened;
import com.bank.accounts.lifecycle.domain.events.AccountStatusChanged;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
@Entity
@Table(name = "accounts")
@Getter
public class Account {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId id;
    
    @Column(nullable = false)
    private String customerId;
    
    @Column(nullable = false)
    private String productType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;
    
    @Column(nullable = false)
    private Instant openedAt;
    
    private Instant closedAt;
    
    @Transient
    private final List<Object> domainEvents = new ArrayList<>();
    
    protected Account() {
        // JPA
    }
    
    private Account(AccountId id, String customerId, String productType) {
        this.id = id;
        this.customerId = customerId;
        this.productType = productType;
        this.status = AccountStatus.ACTIVE;
        this.openedAt = Instant.now();
    }
    
    public static Account open(AccountId id, String customerId, String productType) {
        Account account = new Account(id, customerId, productType);
        account.domainEvents.add(new AccountOpened(id, customerId, productType, account.openedAt));
        return account;
    }
    
    public void changeStatus(AccountStatus newStatus) {
        if (this.status == newStatus) {
            return;
        }
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot change status of a closed account");
        }
        AccountStatus previousStatus = this.status;
        this.status = newStatus;
        this.domainEvents.add(new AccountStatusChanged(this.id, previousStatus, newStatus, Instant.now()));
    }
    
    public void close() {
        if (this.status == AccountStatus.CLOSED) {
            return;
        }
        this.status = AccountStatus.CLOSED;
        this.closedAt = Instant.now();
        this.domainEvents.add(new AccountClosed(this.id, this.closedAt));
    }
    
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
    
    public List<Object> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }
    
    public void clearDomainEvents() {
        domainEvents.clear();
    }
}

