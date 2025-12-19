package com.bank.transfers.initiation.domain;

import com.bank.transfers.initiation.domain.events.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AggregateRoot
@Entity
@Table(name = "transfers")
@Getter
public class Transfer {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "transfer_id"))
    private TransferId transferId;

    @Embedded
    @AttributeOverride(name = "accountId", column = @Column(name = "from_account_id"))
    private AccountRef fromAccountId;

    @Embedded
    private Destination to;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount")),
        @AttributeOverride(name = "currencyCode", column = @Column(name = "currency_code"))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RailType railType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant requestedExecutionDate;

    @Column(length = 1000)
    private String correlationIds; // JSON or comma-separated

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount.amount", column = @Column(name = "fee_amount")),
        @AttributeOverride(name = "amount.currencyCode", column = @Column(name = "fee_currency")),
        @AttributeOverride(name = "type", column = @Column(name = "fee_type"))
    })
    private TransferFee fee;

    @Embedded
    private FailureReason failureReason;

    @Column(nullable = false)
    private String customerId;

    @Column(length = 500)
    private String memo;

    @Version
    private Long version;

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    protected Transfer() {
        // JPA
    }

    private Transfer(TransferId transferId, AccountRef fromAccountId, Destination to, Money amount,
                     RailType railType, String idempotencyKey, Instant requestedExecutionDate,
                     String customerId, String memo, String correlationIds) {
        // Invariants
        if (amount == null || amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (fromAccountId == null) {
            throw new IllegalArgumentException("From account is required");
        }
        if (to == null) {
            throw new IllegalArgumentException("To destination is required");
        }
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }

        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.to = to;
        this.amount = amount;
        this.railType = railType;
        this.status = TransferStatus.DRAFT;
        this.idempotencyKey = idempotencyKey;
        this.createdAt = Instant.now();
        this.requestedExecutionDate = requestedExecutionDate != null ? requestedExecutionDate : Instant.now();
        this.customerId = customerId;
        this.memo = memo;
        this.correlationIds = correlationIds;
        this.fee = TransferFee.zero(amount.getCurrencyCode());
    }

    public static Transfer create(TransferId transferId, AccountRef fromAccountId, Destination to,
                                  Money amount, RailType railType, String idempotencyKey,
                                  Instant requestedExecutionDate, String customerId, String memo,
                                  String correlationIds) {
        Transfer transfer = new Transfer(transferId, fromAccountId, to, amount, railType,
                idempotencyKey, requestedExecutionDate, customerId, memo, correlationIds);
        transfer.domainEvents.add(new TransferCreated(transferId, fromAccountId, to, amount,
                railType, transfer.createdAt));
        return transfer;
    }

    public void submit() {
        if (status != TransferStatus.DRAFT) {
            throw new IllegalStateException("Transfer can only be submitted from DRAFT status. Current status: " + status);
        }
        this.status = TransferStatus.SUBMITTED;
        this.domainEvents.add(new TransferSubmitted(transferId, Instant.now()));
    }

    public void authorize(AuthorizationContext authorizationContext) {
        if (status != TransferStatus.SUBMITTED) {
            throw new IllegalStateException("Transfer can only be authorized from SUBMITTED status. Current status: " + status);
        }
        this.status = TransferStatus.AUTHORIZED;
        this.domainEvents.add(new TransferAuthorized(transferId, authorizationContext, Instant.now()));
    }

    public void reserveFunds(String holdReference) {
        if (status != TransferStatus.AUTHORIZED) {
            throw new IllegalStateException("Funds can only be reserved when transfer is AUTHORIZED. Current status: " + status);
        }
        // Hold reference is stored in correlationIds or as a separate field
        // For simplicity, we'll assume it's tracked externally
        this.status = TransferStatus.PENDING;
        this.domainEvents.add(new TransferExecutionStarted(transferId, holdReference, Instant.now()));
    }

    public void execute() {
        if (status != TransferStatus.PENDING) {
            throw new IllegalStateException("Transfer can only be executed when PENDING. Current status: " + status);
        }
        // Execution logic would be handled by the execution module
        // This method marks the transfer as ready for execution
    }

    public void markCompleted() {
        if (status != TransferStatus.PENDING) {
            throw new IllegalStateException("Transfer can only be completed when PENDING. Current status: " + status);
        }
        this.status = TransferStatus.COMPLETED;
        this.domainEvents.add(new TransferCompleted(transferId, Instant.now()));
    }

    public void fail(FailureReason reason) {
        if (status == TransferStatus.COMPLETED || status == TransferStatus.CANCELLED) {
            throw new IllegalStateException("Cannot fail a transfer that is " + status);
        }
        this.status = TransferStatus.FAILED;
        this.failureReason = reason;
        this.domainEvents.add(new TransferFailed(transferId, reason, Instant.now()));
    }

    public void cancel() {
        if (status == TransferStatus.COMPLETED || status == TransferStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel a transfer that is " + status);
        }
        if (status == TransferStatus.CANCELLED) {
            return; // Already cancelled
        }
        TransferStatus previousStatus = this.status;
        this.status = TransferStatus.CANCELLED;
        this.domainEvents.add(new TransferCancelled(transferId, previousStatus, Instant.now()));
    }

    public void reverse() {
        if (status != TransferStatus.COMPLETED) {
            throw new IllegalStateException("Can only reverse a COMPLETED transfer. Current status: " + status);
        }
        this.status = TransferStatus.REVERSED;
        this.domainEvents.add(new TransferReversed(transferId, Instant.now()));
    }

    public boolean canExecute() {
        return status == TransferStatus.AUTHORIZED || status == TransferStatus.PENDING;
    }

    public boolean isTerminal() {
        return status == TransferStatus.COMPLETED ||
               status == TransferStatus.FAILED ||
               status == TransferStatus.CANCELLED ||
               status == TransferStatus.REVERSED;
    }

    public void applyFee(TransferFee fee) {
        this.fee = fee;
    }

    public List<Object> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}

