package com.bank.accounts.posting.application.port.in;

import com.bank.accounts.posting.domain.PostingId;

public interface ReversePostingUseCase {
    void reversePosting(PostingId postingId);
}

