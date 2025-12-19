package com.bank.transfers.initiation.application.port.in;

import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;

public interface SubmitTransferUseCase {
    Transfer submitTransfer(TransferId transferId);
}

