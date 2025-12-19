package com.bank.accounts.pricing.application.port.out;

import com.bank.accounts.pricing.domain.Fee;

public interface FeeRepositoryPort {
    Fee save(Fee fee);
}

