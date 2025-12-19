package com.bank.accounts.history.adapter.in.events;

import com.bank.accounts.history.application.port.out.TransactionHistoryRepositoryPort;
import com.bank.accounts.history.domain.TransactionEntry;
import com.bank.accounts.posting.domain.PostingStatus;
import com.bank.accounts.posting.domain.events.AccountCredited;
import com.bank.accounts.posting.domain.events.AccountDebited;
import com.bank.accounts.posting.domain.events.PostingReversed;
import com.bank.accounts.pricing.domain.events.FeeAssessed;
import com.bank.accounts.pricing.domain.events.InterestAccrued;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event listener that maintains transaction history read model.
 * Consumes events from posting and pricing modules.
 */
@Component
@RequiredArgsConstructor
public class TransactionHistoryListener {
    
    private final TransactionHistoryRepositoryPort repository;
    
    @EventListener
    @Transactional
    public void handle(AccountDebited event) {
        TransactionEntry entry = new TransactionEntry(
            event.postingId(),
            event.accountId(),
            com.bank.accounts.posting.domain.PostingType.DEBIT,
            PostingStatus.POSTED,
            event.amount(),
            event.description(),
            event.postedAt()
        );
        repository.save(entry);
    }
    
    @EventListener
    @Transactional
    public void handle(AccountCredited event) {
        TransactionEntry entry = new TransactionEntry(
            event.postingId(),
            event.accountId(),
            com.bank.accounts.posting.domain.PostingType.CREDIT,
            PostingStatus.POSTED,
            event.amount(),
            event.description(),
            event.postedAt()
        );
        repository.save(entry);
    }
    
    @EventListener
    @Transactional
    public void handle(PostingReversed event) {
        repository.findByPostingId(event.postingId())
            .ifPresent(entry -> {
                entry.updateStatus(PostingStatus.REVERSED);
                repository.save(entry);
            });
    }
    
    @EventListener
    @Transactional
    public void handle(FeeAssessed event) {
        // Fees are also transactions in the history
        TransactionEntry entry = new TransactionEntry(
            com.bank.accounts.posting.domain.PostingId.generate(), // Generate new ID for fee
            event.accountId(),
            com.bank.accounts.posting.domain.PostingType.DEBIT,
            PostingStatus.POSTED,
            event.amount(),
            "Fee: " + event.description(),
            event.assessedAt()
        );
        repository.save(entry);
    }
    
    @EventListener
    @Transactional
    public void handle(InterestAccrued event) {
        // Interest accruals are also transactions in the history
        TransactionEntry entry = new TransactionEntry(
            com.bank.accounts.posting.domain.PostingId.generate(), // Generate new ID for interest
            event.accountId(),
            com.bank.accounts.posting.domain.PostingType.CREDIT,
            PostingStatus.POSTED,
            event.amount(),
            "Interest: " + event.interestType(),
            event.accruedAt()
        );
        repository.save(entry);
    }
}

