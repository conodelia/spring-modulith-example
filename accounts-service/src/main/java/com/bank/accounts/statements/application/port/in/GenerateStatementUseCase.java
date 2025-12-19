package com.bank.accounts.statements.application.port.in;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.statements.domain.Statement;
import com.bank.accounts.statements.domain.StatementPeriod;

public interface GenerateStatementUseCase {
    Statement generateStatement(AccountId accountId, StatementPeriod period);
}

