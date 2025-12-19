package com.bank.accounts.posting.application.port.out;

import com.bank.accounts.posting.domain.Posting;
import com.bank.accounts.posting.domain.PostingId;

import java.util.Optional;

public interface PostingRepositoryPort {
    Optional<Posting> findById(PostingId postingId);
    Optional<Posting> findByTransactionReference(String transactionReference);
    Posting save(Posting posting);
}

