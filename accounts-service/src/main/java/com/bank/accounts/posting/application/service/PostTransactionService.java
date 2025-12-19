package com.bank.accounts.posting.application.service;

import com.bank.accounts.lifecycle.domain.Account;
import com.bank.accounts.posting.application.port.in.PostTransactionUseCase.PostTransactionCommand;
import com.bank.accounts.posting.application.port.in.PostTransactionUseCase;
import com.bank.accounts.posting.application.port.in.ReversePostingUseCase;
import com.bank.accounts.posting.application.port.out.LoadAccountPort;
import com.bank.accounts.posting.application.port.out.PostingRepositoryPort;
import com.bank.accounts.posting.domain.Posting;
import com.bank.accounts.posting.domain.PostingId;
import com.bank.accounts.posting.domain.PostingType;
import com.bank.accounts.posting.domain.events.AccountCredited;
import com.bank.accounts.posting.domain.events.AccountDebited;
import com.bank.accounts.posting.domain.events.PostingReversed;
import com.bank.accounts.holds.api.HoldsQueryPort;
import com.bank.accounts.pricing.api.PricingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class PostTransactionService implements PostTransactionUseCase, ReversePostingUseCase {
    
    private final PostingRepositoryPort postingRepository;
    private final LoadAccountPort loadAccountPort;
    private final HoldsQueryPort holdsQueryPort;
    private final PricingPort pricingPort;
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public Posting postTransaction(PostTransactionCommand command) {
        // Check idempotency
        if (command.transactionReference() != null) {
            postingRepository.findByTransactionReference(command.transactionReference())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Transaction already exists: " + command.transactionReference());
                });
        }
        
        // Load account
        Account account = loadAccountPort.findById(command.accountId())
            .orElseThrow(() -> new IllegalArgumentException("Account not found: " + command.accountId()));
        
        if (!account.isActive()) {
            throw new IllegalStateException("Cannot post to inactive account: " + command.accountId());
        }
        
        // Validate sufficient funds for debits
        if (command.type() == PostingType.DEBIT) {
            // For now, we'll check holds. Balance check will be done by balances module
            // In a real system, we'd query current balance here
            if (!holdsQueryPort.hasSufficientFunds(command.accountId(), command.amount())) {
                throw new IllegalStateException("Insufficient funds for account: " + command.accountId());
            }
        }
        
        // Create posting
        PostingId postingId = PostingId.generate();
        Posting posting = Posting.create(
            postingId,
            command.accountId(),
            command.type(),
            command.amount(),
            command.description(),
            command.transactionReference()
        );
        
        // Mark as posted
        posting.markAsPosted();
        posting = postingRepository.save(posting);
        
        // Publish domain event
        Instant postedAt = Instant.now();
        if (command.type() == PostingType.DEBIT) {
            AccountDebited event = new AccountDebited(
                postingId,
                command.accountId(),
                command.amount(),
                command.description(),
                postedAt
            );
            applicationEventPublisher.publishEvent(event);
        } else {
            AccountCredited event = new AccountCredited(
                postingId,
                command.accountId(),
                command.amount(),
                command.description(),
                postedAt
            );
            applicationEventPublisher.publishEvent(event);
        }
        
        return posting;
    }
    
    @Override
    public void reversePosting(PostingId postingId) {
        Posting posting = postingRepository.findById(postingId)
            .orElseThrow(() -> new IllegalArgumentException("Posting not found: " + postingId));
        
        if (posting.isReversed()) {
            throw new IllegalStateException("Posting already reversed: " + postingId);
        }
        
        PostingId reversalPostingId = PostingId.generate();
        posting.reverse(reversalPostingId);
        postingRepository.save(posting);
        
        // Create reversal posting (opposite type)
        PostingType reversalType = posting.getType() == PostingType.DEBIT ? PostingType.CREDIT : PostingType.DEBIT;
        Posting reversalPosting = Posting.create(
            reversalPostingId,
            posting.getAccountId(),
            reversalType,
            posting.getAmount(),
            "Reversal of: " + posting.getDescription(),
            null
        );
        reversalPosting.markAsPosted();
        postingRepository.save(reversalPosting);
        
        // Publish reversal event
        PostingReversed event = new PostingReversed(
            postingId,
            reversalPostingId,
            Instant.now()
        );
        applicationEventPublisher.publishEvent(event);
    }
}

