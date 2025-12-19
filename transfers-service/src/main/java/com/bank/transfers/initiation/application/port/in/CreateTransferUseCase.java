package com.bank.transfers.initiation.application.port.in;

import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;

public interface CreateTransferUseCase {
    Transfer createTransfer(CreateTransferCommand command);
    
    record CreateTransferCommand(
        TransferId transferId,
        String fromAccountId,
        String toAccountId, // Can be internal account or external token
        boolean isExternal,
        String amount,
        String currencyCode,
        String railType,
        String idempotencyKey,
        String requestedExecutionDate,
        String customerId,
        String memo,
        String correlationIds
    ) {}
}

