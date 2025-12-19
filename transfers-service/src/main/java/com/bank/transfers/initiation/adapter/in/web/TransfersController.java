package com.bank.transfers.initiation.adapter.in.web;

import com.bank.transfers.initiation.application.port.in.*;
import com.bank.transfers.initiation.domain.Transfer;
import com.bank.transfers.initiation.domain.TransferId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
@Tag(name = "Transfers", description = "Transfer management API")
@RequiredArgsConstructor
public class TransfersController {

    private final CreateTransferUseCase createTransferUseCase;
    private final SubmitTransferUseCase submitTransferUseCase;
    private final AuthorizeTransferUseCase authorizeTransferUseCase;
    private final CancelTransferUseCase cancelTransferUseCase;
    private final GetTransferStatusUseCase getTransferStatusUseCase;
    private final ListTransfersUseCase listTransfersUseCase;

    @PostMapping
    @Operation(summary = "Create a new transfer")
    public ResponseEntity<TransferResponse> createTransfer(@RequestBody CreateTransferRequest request) {
        TransferId transferId = TransferId.generate();
        CreateTransferUseCase.CreateTransferCommand command = new CreateTransferUseCase.CreateTransferCommand(
            transferId,
            request.fromAccountId(),
            request.toAccountId(),
            request.isExternal(),
            request.amount(),
            request.currencyCode(),
            request.railType(),
            request.idempotencyKey(),
            request.requestedExecutionDate(),
            request.customerId(),
            request.memo(),
            request.correlationIds()
        );

        Transfer transfer = createTransferUseCase.createTransfer(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransferResponse.from(transfer));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit a transfer")
    public ResponseEntity<TransferResponse> submitTransfer(@PathVariable String id) {
        Transfer transfer = submitTransferUseCase.submitTransfer(TransferId.of(id));
        return ResponseEntity.ok(TransferResponse.from(transfer));
    }

    @PostMapping("/{id}/authorize")
    @Operation(summary = "Authorize a transfer")
    public ResponseEntity<TransferResponse> authorizeTransfer(
            @PathVariable String id,
            @RequestBody AuthorizeTransferRequest request) {
        AuthorizeTransferUseCase.AuthorizeTransferCommand command = new AuthorizeTransferUseCase.AuthorizeTransferCommand(
            TransferId.of(id),
            request.toAuthorizationContext()
        );
        Transfer transfer = authorizeTransferUseCase.authorizeTransfer(command);
        return ResponseEntity.ok(TransferResponse.from(transfer));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel a transfer")
    public ResponseEntity<Void> cancelTransfer(@PathVariable String id) {
        cancelTransferUseCase.cancelTransfer(TransferId.of(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transfer status")
    public ResponseEntity<TransferResponse> getTransferStatus(@PathVariable String id) {
        return getTransferStatusUseCase.getTransferStatus(TransferId.of(id))
            .map(TransferResponse::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "List transfers for a customer")
    public ResponseEntity<List<TransferResponse>> listTransfers(@RequestParam String customerId) {
        List<Transfer> transfers = listTransfersUseCase.listTransfers(customerId);
        List<TransferResponse> responses = transfers.stream()
            .map(TransferResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    // DTOs
    public record CreateTransferRequest(
        String fromAccountId,
        String toAccountId,
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

    public record AuthorizeTransferRequest(
        String method,
        String authStrength,
        Integer deviceRiskScore
    ) {
        com.bank.transfers.initiation.domain.AuthorizationContext toAuthorizationContext() {
            return com.bank.transfers.initiation.domain.AuthorizationContext.of(
                com.bank.transfers.initiation.domain.AuthorizationMethod.valueOf(method),
                com.bank.transfers.initiation.domain.AuthStrength.valueOf(authStrength),
                deviceRiskScore
            );
        }
    }

    public record TransferResponse(
        String transferId,
        String fromAccountId,
        String status,
        String amount,
        String currencyCode,
        String railType,
        String createdAt
    ) {
        static TransferResponse from(Transfer transfer) {
            return new TransferResponse(
                transfer.getTransferId().toString(),
                transfer.getFromAccountId().toString(),
                transfer.getStatus().name(),
                transfer.getAmount().getAmount().toString(),
                transfer.getAmount().getCurrencyCode(),
                transfer.getRailType().name(),
                transfer.getCreatedAt().toString()
            );
        }
    }
}

