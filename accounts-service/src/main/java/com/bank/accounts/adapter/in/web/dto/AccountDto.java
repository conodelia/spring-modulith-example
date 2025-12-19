package com.bank.accounts.adapter.in.web.dto;

import com.bank.accounts.lifecycle.domain.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Account information")
public record AccountDto(
    @Schema(description = "Account ID", example = "123e4567-e89b-12d3-a456-426614174000")
    String accountId,
    @Schema(description = "Customer ID", example = "customer-123")
    String customerId,
    @Schema(description = "Product type", example = "CHECKING")
    String productType,
    @Schema(description = "Account status", example = "ACTIVE")
    AccountStatus status,
    @Schema(description = "Account opened date")
    Instant openedAt,
    @Schema(description = "Account closed date", nullable = true)
    Instant closedAt
) {
    public static AccountDto from(com.bank.accounts.lifecycle.domain.Account account) {
        return new AccountDto(
            account.getId().toString(),
            account.getCustomerId(),
            account.getProductType(),
            account.getStatus(),
            account.getOpenedAt(),
            account.getClosedAt()
        );
    }
}

