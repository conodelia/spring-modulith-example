package com.bank.accounts.statements.adapter.in.events;

import com.bank.accounts.posting.domain.events.AccountCredited;
import com.bank.accounts.posting.domain.events.AccountDebited;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listener for statement generation.
 * In a real system, this would trigger statement generation periodically
 * or based on specific events. For now, this is a placeholder.
 */
@Component
@RequiredArgsConstructor
public class StatementEventListener {
    
    @EventListener
    public void handle(AccountDebited event) {
        // In a real system, this would track transactions for statement generation
        // For now, statements are generated on-demand
    }
    
    @EventListener
    public void handle(AccountCredited event) {
        // In a real system, this would track transactions for statement generation
        // For now, statements are generated on-demand
    }
}

