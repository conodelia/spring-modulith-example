package com.bank.transfers.initiation.application.port.in;

import com.bank.transfers.initiation.domain.AuthorizationContext;
import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;

public interface AuthorizeTransferUseCase {
    Transfer authorizeTransfer(AuthorizeTransferCommand command);
    
    record AuthorizeTransferCommand(
        TransferId transferId,
        AuthorizationContext authorizationContext
    ) {}
}

