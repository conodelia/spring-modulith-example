package com.bank.accounts.adapter.in.web.dto;

import com.bank.accounts.posting.domain.PostingStatus;
import com.bank.accounts.posting.domain.PostingType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Transaction information")
public record TransactionDto(
    @Schema(description = "Transaction ID", example = "123e4567-e89b-12d3-a456-426614174000")
    String transactionId,
    @Schema(description = "Account ID", example = "123e4567-e89b-12d3-a456-426614174000")
    String accountId,
    @Schema(description = "Transaction type", example = "DEBIT")
    PostingType type,
    @Schema(description = "Transaction status", example = "POSTED")
    PostingStatus status,
    @Schema(description = "Transaction amount", example = "100.00")
    BigDecimal amount,
    @Schema(description = "Transaction description", example = "Purchase at store")
    String description,
    @Schema(description = "Transaction date")
    Instant postedAt
) {
    public static TransactionDto from(com.bank.accounts.history.domain.TransactionEntry entry) {
        return new TransactionDto(
            entry.getPostingId().toString(),
            entry.getAccountId().toString(),
            entry.getType(),
            entry.getStatus(),
            entry.getAmount(),
            entry.getDescription(),
            entry.getPostedAt()
        );
    }
}

