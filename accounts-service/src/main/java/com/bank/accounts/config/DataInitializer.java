package com.bank.accounts.config;

import com.bank.accounts.lifecycle.application.port.in.OpenAccountUseCase;
import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.posting.application.port.in.PostTransactionUseCase;
import com.bank.accounts.posting.domain.PostingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final OpenAccountUseCase openAccountUseCase;
    private final PostTransactionUseCase postTransactionUseCase;
    
    @Bean
    ApplicationRunner initializeData() {
        return args -> {
            log.info("Initializing test data...");
            
            // Create one debit account (checking account)
            AccountId accountId = AccountId.generate();
            var account = openAccountUseCase.openAccount(
                new OpenAccountUseCase.OpenAccountCommand(
                    accountId,
                    "customer-123",
                    "CHECKING"
                )
            );
            log.info("Created account: {}", account.getId());
            
            // Create 12 transactions with mix of debits and credits
            Instant baseDate = Instant.now().minus(30, ChronoUnit.DAYS);
            
            // Transaction 1: Credit - Initial deposit
            postTransaction(accountId, PostingType.CREDIT, new BigDecimal("1000.00"), 
                "Initial deposit", baseDate, "txn-001");
            
            // Transaction 2: Debit - Purchase
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("50.00"), 
                "Purchase at Store A", baseDate.plus(1, ChronoUnit.DAYS), "txn-002");
            
            // Transaction 3: Debit - ATM withdrawal
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("100.00"), 
                "ATM withdrawal", baseDate.plus(2, ChronoUnit.DAYS), "txn-003");
            
            // Transaction 4: Credit - Salary deposit
            postTransaction(accountId, PostingType.CREDIT, new BigDecimal("2500.00"), 
                "Salary deposit", baseDate.plus(5, ChronoUnit.DAYS), "txn-004");
            
            // Transaction 5: Debit - Online purchase
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("75.50"), 
                "Online purchase", baseDate.plus(7, ChronoUnit.DAYS), "txn-005");
            
            // Transaction 6: Credit - Refund
            postTransaction(accountId, PostingType.CREDIT, new BigDecimal("25.00"), 
                "Refund from Store B", baseDate.plus(8, ChronoUnit.DAYS), "txn-006");
            
            // Transaction 7: Debit - Utility bill
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("120.00"), 
                "Utility bill payment", baseDate.plus(10, ChronoUnit.DAYS), "txn-007");
            
            // Transaction 8: Debit - Restaurant
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("45.75"), 
                "Restaurant payment", baseDate.plus(12, ChronoUnit.DAYS), "txn-008");
            
            // Transaction 9: Credit - Transfer received
            postTransaction(accountId, PostingType.CREDIT, new BigDecimal("200.00"), 
                "Transfer received", baseDate.plus(15, ChronoUnit.DAYS), "txn-009");
            
            // Transaction 10: Debit - Gas station
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("35.00"), 
                "Gas station payment", baseDate.plus(18, ChronoUnit.DAYS), "txn-010");
            
            // Transaction 11: Credit - Interest payment
            postTransaction(accountId, PostingType.CREDIT, new BigDecimal("5.25"), 
                "Interest payment", baseDate.plus(20, ChronoUnit.DAYS), "txn-011");
            
            // Transaction 12: Debit - Subscription
            postTransaction(accountId, PostingType.DEBIT, new BigDecimal("9.99"), 
                "Monthly subscription", baseDate.plus(25, ChronoUnit.DAYS), "txn-012");
            
            log.info("Data initialization completed. Account ID: {}", accountId);
        };
    }
    
    private void postTransaction(AccountId accountId, PostingType type, BigDecimal amount, 
                                 String description, Instant postedAt, String transactionReference) {
        try {
            // Note: In a real system, we'd need to adjust the posting date
            // For now, we'll use the current time and let the system handle it
            postTransactionUseCase.postTransaction(
                new PostTransactionUseCase.PostTransactionCommand(
                    accountId,
                    type,
                    amount,
                    description,
                    transactionReference
                )
            );
            log.info("Posted transaction: {} {} - {}", type, amount, description);
        } catch (Exception e) {
            log.error("Failed to post transaction: {}", e.getMessage(), e);
        }
    }
}

