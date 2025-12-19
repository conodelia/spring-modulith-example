package com.bank.accounts.statements.application.port.out;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.statements.domain.Statement;
import com.bank.accounts.statements.domain.StatementId;
import com.bank.accounts.statements.domain.StatementPeriod;

import java.util.Optional;

public interface StatementRepositoryPort {
    Optional<Statement> findById(StatementId statementId);
    Optional<Statement> findByAccountIdAndPeriod(AccountId accountId, StatementPeriod period);
    Statement save(Statement statement);
}

