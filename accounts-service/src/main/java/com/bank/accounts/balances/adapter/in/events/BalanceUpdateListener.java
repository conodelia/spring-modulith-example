package com.bank.accounts.balances.adapter.in.events;

import com.bank.accounts.balances.application.port.out.BalanceRepositoryPort;
import com.bank.accounts.balances.domain.AccountBalance;
import com.bank.accounts.balances.domain.events.BalanceUpdated;
import com.bank.accounts.posting.domain.events.AccountCredited;
import com.bank.accounts.posting.domain.events.AccountDebited;
import com.bank.accounts.posting.domain.events.PostingReversed;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Event listener that updates account balances based on posting events.
 * This is an inbound adapter that consumes events from the posting module.
 */
@Component
@RequiredArgsConstructor
public class BalanceUpdateListener {
    
    private final BalanceRepositoryPort balanceRepository;
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher;
    
    @EventListener
    @Transactional
    public void handle(AccountDebited event) {
        AccountBalance balance = getOrCreateBalance(event.accountId());
        balance.applyDebit(event.amount());
        balance = balanceRepository.save(balance);
        publishBalanceUpdated(event.accountId(), balance);
    }
    
    @EventListener
    @Transactional
    public void handle(AccountCredited event) {
        AccountBalance balance = getOrCreateBalance(event.accountId());
        balance.applyCredit(event.amount());
        balance = balanceRepository.save(balance);
        publishBalanceUpdated(event.accountId(), balance);
    }
    
    @EventListener
    @Transactional
    public void handle(PostingReversed event) {
        // For reversals, we need to get the original posting to determine the reversal
        // For simplicity, we'll handle this by listening to the reversal and adjusting
        // In a real system, we'd query the posting to get the amount and type
        // For now, this is a placeholder - the actual reversal logic would need the posting details
    }
    
    private AccountBalance getOrCreateBalance(com.bank.accounts.lifecycle.domain.AccountId accountId) {
        return balanceRepository.findByAccountId(accountId)
            .orElseGet(() -> new AccountBalance(accountId));
    }
    
    private void publishBalanceUpdated(com.bank.accounts.lifecycle.domain.AccountId accountId, AccountBalance balance) {
        BalanceUpdated event = new BalanceUpdated(
            accountId,
            balance.getCurrentBalance(),
            balance.getAvailableBalance(),
            Instant.now()
        );
        applicationEventPublisher.publishEvent(event);
    }
}

