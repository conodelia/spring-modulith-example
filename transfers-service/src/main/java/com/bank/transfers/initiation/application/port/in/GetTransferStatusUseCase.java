package com.bank.transfers.initiation.application.port.in;

import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;

import java.util.Optional;

public interface GetTransferStatusUseCase {
    Optional<Transfer> getTransferStatus(TransferId transferId);
}

