package com.bank.transfers.initiation.application.service;

import com.bank.transfers.fees.domain.TransferPricingService;
import com.bank.transfers.initiation.application.port.in.*;
import com.bank.transfers.initiation.application.port.out.AccountsPort;
import com.bank.transfers.initiation.application.port.out.IdentityPort;
import com.bank.transfers.initiation.application.port.out.RiskPort;
import com.bank.transfers.initiation.application.port.out.TransferRepositoryPort;
import com.bank.transfers.initiation.domain.*;
import com.bank.transfers.routing.domain.TransferRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService implements CreateTransferUseCase, SubmitTransferUseCase,
        AuthorizeTransferUseCase, CancelTransferUseCase, GetTransferStatusUseCase, ListTransfersUseCase {

    private final TransferRepositoryPort transferRepository;
    private final TransferRoutingService routingService;
    private final TransferPricingService pricingService;
    private final AccountsPort accountsPort;
    private final IdentityPort identityPort;
    private final RiskPort riskPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Transfer createTransfer(CreateTransferCommand command) {
        // Check idempotency
        Optional<Transfer> existing = transferRepository.findByIdempotencyKey(command.idempotencyKey());
        if (existing.isPresent()) {
            return existing.get();
        }

        // Build value objects
        AccountRef fromAccountRef = AccountRef.of(command.fromAccountId());
        Destination to;
        if (command.isExternal()) {
            to = Destination.external(ExternalDestinationRef.of(command.toAccountId()));
        } else {
            to = Destination.internal(AccountRef.of(command.toAccountId()));
        }

        Money amount = Money.of(new BigDecimal(command.amount()), command.currencyCode());
        RailType railType = RailType.valueOf(command.railType());
        
        // Determine rail if not specified
        if (railType == null) {
            railType = routingService.determineRail(to, amount, command.currencyCode(), 
                command.requestedExecutionDate() != null ? Instant.parse(command.requestedExecutionDate()) : null);
        }

        Instant requestedExecutionDate = command.requestedExecutionDate() != null 
            ? Instant.parse(command.requestedExecutionDate()) 
            : Instant.now();

        // Create transfer
        Transfer transfer = Transfer.create(
            command.transferId(),
            fromAccountRef,
            to,
            amount,
            railType,
            command.idempotencyKey(),
            requestedExecutionDate,
            command.customerId(),
            command.memo(),
            command.correlationIds()
        );

        // Calculate and apply fee
        TransferFee fee = pricingService.calculateFee(railType, amount);
        transfer.applyFee(fee);

        transfer = transferRepository.save(transfer);
        publishDomainEvents(transfer);
        return transfer;
    }

    @Override
    public Transfer submitTransfer(TransferId transferId) {
        Transfer transfer = transferRepository.findById(transferId)
            .orElseThrow(() -> new IllegalArgumentException("Transfer not found: " + transferId));

        // Verify customer entitlement
        if (!identityPort.verifyCustomerEntitled(transfer.getCustomerId(), transfer.getFromAccountId())) {
            throw new IllegalStateException("Customer not entitled to use this account");
        }

        transfer.submit();
        transfer = transferRepository.save(transfer);
        publishDomainEvents(transfer);
        return transfer;
    }

    @Override
    public Transfer authorizeTransfer(AuthorizeTransferCommand command) {
        Transfer transfer = transferRepository.findById(command.transferId())
            .orElseThrow(() -> new IllegalArgumentException("Transfer not found: " + command.transferId()));

        transfer.authorize(command.authorizationContext());
        transfer = transferRepository.save(transfer);
        publishDomainEvents(transfer);
        return transfer;
    }

    @Override
    public Transfer cancelTransfer(TransferId transferId) {
        Transfer transfer = transferRepository.findById(transferId)
            .orElseThrow(() -> new IllegalArgumentException("Transfer not found: " + transferId));

        transfer.cancel();
        transfer = transferRepository.save(transfer);
        publishDomainEvents(transfer);
        return transfer;
    }

    @Override
    public Optional<Transfer> getTransferStatus(TransferId transferId) {
        return transferRepository.findById(transferId);
    }

    @Override
    public List<Transfer> listTransfers(String customerId) {
        return transferRepository.findByCustomerId(customerId);
    }

    private void publishDomainEvents(Transfer transfer) {
        List<Object> events = transfer.getDomainEvents();
        events.forEach(event -> eventPublisher.publishEvent(event));
        transfer.clearDomainEvents();
    }
}

