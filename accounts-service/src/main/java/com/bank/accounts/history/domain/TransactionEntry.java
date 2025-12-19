package com.bank.accounts.history.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.domain.PostingId;
import com.bank.accounts.posting.domain.PostingStatus;
import com.bank.accounts.posting.domain.PostingType;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transaction_history")
@Getter
public class TransactionEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "posting_id"))
    private PostingId postingId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "account_id"))
    private AccountId accountId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostingType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostingStatus status;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Instant postedAt;
    
    protected TransactionEntry() {
        // JPA
    }
    
    public TransactionEntry(PostingId postingId, AccountId accountId, PostingType type, 
                           PostingStatus status, BigDecimal amount, String description, Instant postedAt) {
        this.postingId = postingId;
        this.accountId = accountId;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.description = description;
        this.postedAt = postedAt;
    }
    
    public void updateStatus(PostingStatus newStatus) {
        this.status = newStatus;
    }
}

