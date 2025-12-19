package com.bank.accounts.adapter.in.web;

import com.bank.accounts.adapter.in.web.dto.AccountDto;
import com.bank.accounts.adapter.in.web.dto.TransactionDto;
import com.bank.accounts.history.application.port.in.GetTransactionHistoryUseCase;
import com.bank.accounts.lifecycle.application.port.in.GetAllAccountsUseCase;
import com.bank.accounts.lifecycle.application.port.out.LoadAccountPort;
import com.bank.accounts.lifecycle.domain.AccountId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Account management API")
@RequiredArgsConstructor
public class AccountsController {
    
    private final LoadAccountPort loadAccountPort;
    private final GetTransactionHistoryUseCase getTransactionHistoryUseCase;
    private final GetAllAccountsUseCase getAllAccountsUseCase;
    
    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieves a list of all accounts")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of accounts")
    })
    public ResponseEntity<List<AccountDto>> getAccounts() {
        List<AccountDto> accounts = getAllAccountsUseCase.getAllAccounts()
            .stream()
            .map(AccountDto::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(accounts);
    }
    
    @GetMapping("/{accountId}/transactions")
    @Operation(summary = "Get account transactions", description = "Retrieves transaction history for a specific account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved transaction history"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<List<TransactionDto>> getTransactions(
        @Parameter(description = "Account ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String accountId
    ) {
        AccountId id = AccountId.of(accountId);
        
        // Verify account exists
        if (loadAccountPort.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        List<TransactionDto> transactions = getTransactionHistoryUseCase.getTransactionHistory(id)
            .stream()
            .map(TransactionDto::from)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(transactions);
    }
}

