package com.bank.transfers.initiation.application.port.in;

import com.bank.transfers.initiation.domain.Transfer;

import java.util.List;

public interface ListTransfersUseCase {
    List<Transfer> listTransfers(String customerId);
}

