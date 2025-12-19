package com.bank.accounts.pricing.application.port.out;

import com.bank.accounts.pricing.domain.InterestAccrual;

public interface InterestRepositoryPort {
    InterestAccrual save(InterestAccrual accrual);
}

