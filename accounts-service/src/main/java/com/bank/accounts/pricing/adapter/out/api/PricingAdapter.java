package com.bank.accounts.pricing.adapter.out.api;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.pricing.api.PricingPort;
import com.bank.accounts.pricing.application.port.in.CalculateFeeUseCase;
import com.bank.accounts.posting.domain.PostingType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Adapter that implements the published PricingPort API.
 * This allows other modules (like posting) to calculate fees.
 */
@Component
@RequiredArgsConstructor
public class PricingAdapter implements PricingPort {
    
    private final CalculateFeeUseCase calculateFeeUseCase;
    
    @Override
    public BigDecimal calculateFee(AccountId accountId, PostingType type, BigDecimal amount) {
        CalculateFeeUseCase.AssessFeeCommand command = new CalculateFeeUseCase.AssessFeeCommand(
            accountId,
            type,
            amount
        );
        var fee = calculateFeeUseCase.assessFee(command);
        return fee != null ? fee.getAmount() : BigDecimal.ZERO;
    }
    
    @Override
    public boolean shouldApplyFee(AccountId accountId, PostingType type, BigDecimal amount) {
        BigDecimal fee = calculateFee(accountId, type, amount);
        return fee.compareTo(BigDecimal.ZERO) > 0;
    }
}

