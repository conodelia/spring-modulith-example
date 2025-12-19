package com.bank.transfers.beneficiaries.domain;

import com.bank.transfers.beneficiaries.domain.events.*;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AggregateRoot
@Entity
@Table(name = "beneficiaries")
@Getter
public class Beneficiary {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "beneficiary_id"))
    private BeneficiaryId beneficiaryId;

    @Column(nullable = false)
    private String customerId;

    @Embedded
    private DestinationDetails destinationDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BeneficiaryStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant verifiedAt;
    private Instant blockedAt;

    @Version
    private Long version;

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    protected Beneficiary() {
        // JPA
    }

    private Beneficiary(BeneficiaryId beneficiaryId, String customerId, DestinationDetails destinationDetails) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (destinationDetails == null) {
            throw new IllegalArgumentException("Destination details are required");
        }

        this.beneficiaryId = beneficiaryId;
        this.customerId = customerId;
        this.destinationDetails = destinationDetails;
        this.status = BeneficiaryStatus.PENDING_VERIFICATION;
        this.createdAt = Instant.now();
    }

    public static Beneficiary enroll(BeneficiaryId beneficiaryId, String customerId, DestinationDetails destinationDetails) {
        Beneficiary beneficiary = new Beneficiary(beneficiaryId, customerId, destinationDetails);
        beneficiary.domainEvents.add(new BeneficiaryEnrolled(beneficiaryId, customerId, destinationDetails, beneficiary.createdAt));
        return beneficiary;
    }

    public void verify() {
        if (status != BeneficiaryStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException("Beneficiary can only be verified from PENDING_VERIFICATION status. Current status: " + status);
        }
        this.status = BeneficiaryStatus.ACTIVE;
        this.verifiedAt = Instant.now();
        this.domainEvents.add(new BeneficiaryVerified(beneficiaryId, this.verifiedAt));
    }

    public void block() {
        if (status == BeneficiaryStatus.BLOCKED) {
            return; // Already blocked
        }
        BeneficiaryStatus previousStatus = this.status;
        this.status = BeneficiaryStatus.BLOCKED;
        this.blockedAt = Instant.now();
        this.domainEvents.add(new BeneficiaryBlocked(beneficiaryId, previousStatus, this.blockedAt));
    }

    public boolean isActive() {
        return status == BeneficiaryStatus.ACTIVE;
    }

    public List<Object> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}

