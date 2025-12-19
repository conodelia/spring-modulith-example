package com.bank.transfers.config;

import com.bank.transfers.beneficiaries.application.port.in.AddBeneficiaryUseCase;
import com.bank.transfers.beneficiaries.domain.DestinationDetails;
import com.bank.transfers.initiation.application.port.in.CreateTransferUseCase;
import com.bank.transfers.initiation.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CreateTransferUseCase createTransferUseCase;
    private final AddBeneficiaryUseCase addBeneficiaryUseCase;

    @Override
    public void run(String... args) {
        // Seed a beneficiary
        String customerId = "customer-123";
        addBeneficiaryUseCase.addBeneficiary(new AddBeneficiaryUseCase.AddBeneficiaryCommand(
            customerId,
            "token-ref-001",
            "John Doe",
            "Bank ABC"
        ));

        // Seed a few transfers
        String fromAccountId = UUID.randomUUID().toString();
        String toAccountId = UUID.randomUUID().toString();

        // Transfer 1: Internal transfer
        createTransferUseCase.createTransfer(new CreateTransferUseCase.CreateTransferCommand(
            TransferId.generate(),
            fromAccountId,
            toAccountId,
            false, // internal
            "100.00",
            "USD",
            "INTERNAL",
            "idempotency-key-1",
            Instant.now().toString(),
            customerId,
            "Test transfer 1",
            "correlation-1"
        ));

        // Transfer 2: External ACH transfer
        createTransferUseCase.createTransfer(new CreateTransferUseCase.CreateTransferCommand(
            TransferId.generate(),
            fromAccountId,
            "external-token-001",
            true, // external
            "500.00",
            "USD",
            "ACH",
            "idempotency-key-2",
            Instant.now().toString(),
            customerId,
            "Test transfer 2",
            "correlation-2"
        ));

        // Transfer 3: Wire transfer
        createTransferUseCase.createTransfer(new CreateTransferUseCase.CreateTransferCommand(
            TransferId.generate(),
            fromAccountId,
            "external-token-002",
            true, // external
            "5000.00",
            "USD",
            "WIRE",
            "idempotency-key-3",
            Instant.now().plusSeconds(3600).toString(), // Future execution
            customerId,
            "Test transfer 3",
            "correlation-3"
        ));
    }
}

