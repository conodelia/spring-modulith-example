package com.bank.accounts.holds.application.service;

import com.bank.accounts.holds.application.port.in.PlaceHoldUseCase;
import com.bank.accounts.holds.application.port.in.ReleaseHoldUseCase;
import com.bank.accounts.holds.application.port.out.HoldRepositoryPort;
import com.bank.accounts.holds.domain.Hold;
import com.bank.accounts.holds.domain.HoldId;
import com.bank.accounts.holds.domain.events.HoldPlaced;
import com.bank.accounts.holds.domain.events.HoldReleased;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class HoldService implements PlaceHoldUseCase, ReleaseHoldUseCase {
    
    private final HoldRepositoryPort holdRepository;
    private final org.springframework.context.ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    public Hold placeHold(PlaceHoldCommand command) {
        HoldId holdId = HoldId.generate();
        Hold hold = Hold.place(
            holdId,
            command.accountId(),
            command.type(),
            command.amount(),
            command.reason(),
            command.expiresAt()
        );
        hold = holdRepository.save(hold);
        
        // Publish event
        HoldPlaced event = new HoldPlaced(
            holdId,
            command.accountId(),
            command.amount(),
            Instant.now()
        );
        applicationEventPublisher.publishEvent(event);
        
        return hold;
    }
    
    @Override
    public void releaseHold(HoldId holdId) {
        Hold hold = holdRepository.findById(holdId)
            .orElseThrow(() -> new IllegalArgumentException("Hold not found: " + holdId));
        hold.release();
        holdRepository.save(hold);
        
        // Publish event
        HoldReleased event = new HoldReleased(
            holdId,
            hold.getAccountId(),
            Instant.now()
        );
        applicationEventPublisher.publishEvent(event);
    }
}

