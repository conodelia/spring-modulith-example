package com.bank.accounts.posting.domain;

import com.bank.accounts.lifecycle.domain.AccountId;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "postings")
@Getter
public class Posting {
    
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "posting_id"))
    private PostingId id;
    
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
    
    private Instant reversedAt;
    
    private PostingId reversalPostingId;
    
    @Column(unique = true)
    private String transactionReference;
    
    protected Posting() {
        // JPA
    }
    
    private Posting(PostingId id, AccountId accountId, PostingType type, BigDecimal amount, 
                   String description, String transactionReference) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.status = PostingStatus.PENDING;
        this.postedAt = Instant.now();
        this.transactionReference = transactionReference;
    }
    
    public static Posting create(PostingId id, AccountId accountId, PostingType type, 
                                BigDecimal amount, String description, String transactionReference) {
        return new Posting(id, accountId, type, amount, description, transactionReference);
    }
    
    public void markAsPosted() {
        if (this.status != PostingStatus.PENDING) {
            throw new IllegalStateException("Only pending postings can be marked as posted");
        }
        this.status = PostingStatus.POSTED;
    }
    
    public void reverse(PostingId reversalPostingId) {
        if (this.status != PostingStatus.POSTED) {
            throw new IllegalStateException("Only posted postings can be reversed");
        }
        this.status = PostingStatus.REVERSED;
        this.reversedAt = Instant.now();
        this.reversalPostingId = reversalPostingId;
    }
    
    public boolean isPosted() {
        return status == PostingStatus.POSTED;
    }
    
    public boolean isReversed() {
        return status == PostingStatus.REVERSED;
    }
}

