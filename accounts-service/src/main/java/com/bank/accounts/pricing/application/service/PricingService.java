package com.bank.accounts.pricing.application.service;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.pricing.application.port.in.AccrueInterestUseCase;
import com.bank.accounts.pricing.application.port.in.CalculateFeeUseCase;
import com.bank.accounts.pricing.application.port.out.FeeRepositoryPort;
import com.bank.accounts.pricing.application.port.out.InterestRepositoryPort;
import com.bank.accounts.pricing.domain.Fee;
import com.bank.accounts.pricing.domain.FeeType;
import com.bank.accounts.pricing.domain.InterestAccrual;
import com.bank.accounts.pricing.domain.InterestType;
import com.bank.accounts.pricing.domain.events.FeeAssessed;
import com.bank.accounts.pricing.domain.events.InterestAccrued;
import com.bank.accounts.posting.domain.PostingType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class PricingService implements CalculateFeeUseCase, AccrueInterestUseCase {
    
    private final FeeRepositoryPort feeRepository;
    private final InterestRepositoryPort interestRepository;
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public Fee assessFee(AssessFeeCommand command) {
        // Simple fee calculation logic
        BigDecimal feeAmount = calculateFeeAmount(command.postingType(), command.amount());
        
        if (feeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return null; // No fee
        }
        
        FeeType feeType = determineFeeType(command.postingType());
        Fee fee = new Fee(
            command.accountId(),
            feeType,
            feeAmount,
            "Fee for " + command.postingType() + " transaction"
        );
        fee = feeRepository.save(fee);
        
        // Publish event
        FeeAssessed event = new FeeAssessed(
            command.accountId(),
            feeAmount,
            feeType,
            fee.getDescription(),
            Instant.now()
        );
        applicationEventPublisher.publishEvent(event);
        
        return fee;
    }
    
    @Override
    public InterestAccrual accrueInterest(AccountId accountId, InterestType type, BigDecimal amount) {
        InterestAccrual accrual = new InterestAccrual(accountId, type, amount);
        accrual = interestRepository.save(accrual);
        
        // Publish event
        InterestAccrued event = new InterestAccrued(
            accountId,
            amount,
            type,
            Instant.now()
        );
        applicationEventPublisher.publishEvent(event);
        
        return accrual;
    }
    
    private BigDecimal calculateFeeAmount(PostingType postingType, BigDecimal amount) {
        // Simple fee calculation: 1% for debits, 0.5% for credits, minimum $0.50
        BigDecimal percentage = postingType == PostingType.DEBIT 
            ? new BigDecimal("0.01") 
            : new BigDecimal("0.005");
        BigDecimal calculatedFee = amount.multiply(percentage);
        BigDecimal minimumFee = new BigDecimal("0.50");
        return calculatedFee.max(minimumFee);
    }
    
    private FeeType determineFeeType(PostingType postingType) {
        return postingType == PostingType.DEBIT 
            ? FeeType.TRANSACTION_FEE 
            : FeeType.SERVICE_FEE;
    }
}

