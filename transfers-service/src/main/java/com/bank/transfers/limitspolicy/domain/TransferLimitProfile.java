package com.bank.transfers.limitspolicy.domain;

import com.bank.transfers.initiation.domain.Money;
import jakarta.persistence.*;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@AggregateRoot
@Entity
@Table(name = "transfer_limit_profiles")
@Getter
public class TransferLimitProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String customerId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "daily_limit_amount")),
        @AttributeOverride(name = "currencyCode", column = @Column(name = "daily_limit_currency"))
    })
    private Money dailyLimit;

    @ElementCollection
    @CollectionTable(name = "velocity_limits", joinColumns = @JoinColumn(name = "profile_id"))
    @MapKeyColumn(name = "period")
    @Column(name = "limit_amount")
    private Map<String, BigDecimal> velocityLimits = new HashMap<>(); // e.g., "HOURLY" -> amount

    @ElementCollection
    @CollectionTable(name = "channel_limits", joinColumns = @JoinColumn(name = "profile_id"))
    @MapKeyColumn(name = "channel")
    @Column(name = "limit_amount")
    private Map<String, BigDecimal> channelLimits = new HashMap<>(); // e.g., "MOBILE" -> amount

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @Version
    private Long version;

    protected TransferLimitProfile() {
        // JPA
    }

    private TransferLimitProfile(String customerId, Money dailyLimit) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (dailyLimit == null) {
            throw new IllegalArgumentException("Daily limit is required");
        }

        this.customerId = customerId;
        this.dailyLimit = dailyLimit;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static TransferLimitProfile create(String customerId, Money dailyLimit) {
        return new TransferLimitProfile(customerId, dailyLimit);
    }

    public boolean checkLimit(Money transferAmount, String channel) {
        // Check daily limit
        if (transferAmount.isGreaterThan(dailyLimit)) {
            return false;
        }

        // Check channel limit if exists
        if (channel != null && channelLimits.containsKey(channel)) {
            BigDecimal channelLimit = channelLimits.get(channel);
            if (transferAmount.getAmount().compareTo(channelLimit) > 0) {
                return false;
            }
        }

        return true;
    }

    public void recordTransfer(Money transferAmount) {
        // In a real system, this would update daily/velocity counters
        // For now, we just update the timestamp
        this.updatedAt = Instant.now();
    }

    public void setVelocityLimit(String period, BigDecimal amount) {
        if (period == null || period.trim().isEmpty()) {
            throw new IllegalArgumentException("Period is required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.velocityLimits.put(period, amount);
        this.updatedAt = Instant.now();
    }

    public void setChannelLimit(String channel, BigDecimal amount) {
        if (channel == null || channel.trim().isEmpty()) {
            throw new IllegalArgumentException("Channel is required");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        this.channelLimits.put(channel, amount);
        this.updatedAt = Instant.now();
    }

    public void updateDailyLimit(Money newLimit) {
        if (newLimit == null) {
            throw new IllegalArgumentException("Daily limit is required");
        }
        this.dailyLimit = newLimit;
        this.updatedAt = Instant.now();
    }
}

