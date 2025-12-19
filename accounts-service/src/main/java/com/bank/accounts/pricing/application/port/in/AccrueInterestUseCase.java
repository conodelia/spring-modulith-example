package com.bank.accounts.pricing.application.port.in;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.pricing.domain.InterestAccrual;
import com.bank.accounts.pricing.domain.InterestType;

import java.math.BigDecimal;

public interface AccrueInterestUseCase {
    InterestAccrual accrueInterest(AccountId accountId, InterestType type, BigDecimal amount);
}

