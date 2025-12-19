package com.bank.accounts.statements.adapter.out.persistence;

import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.statements.domain.Statement;
import com.bank.accounts.statements.domain.StatementId;
import com.bank.accounts.statements.domain.StatementPeriod;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;

@Repository
public interface JpaStatementRepository extends JpaRepository<Statement, StatementId> {
    Optional<Statement> findById(StatementId id);
    
    @Query("SELECT s FROM Statement s WHERE s.accountId = :accountId AND s.period = :period")
    Optional<Statement> findByAccountIdAndPeriod(@Param("accountId") AccountId accountId, @Param("period") YearMonth period);
}

@Component
class StatementRepositoryAdapter implements com.bank.accounts.statements.application.port.out.StatementRepositoryPort {
    
    private final JpaStatementRepository repository;
    
    StatementRepositoryAdapter(JpaStatementRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Optional<Statement> findById(StatementId statementId) {
        return repository.findById(statementId);
    }
    
    @Override
    public Optional<Statement> findByAccountIdAndPeriod(AccountId accountId, StatementPeriod period) {
        return repository.findByAccountIdAndPeriod(accountId, period.yearMonth());
    }
    
    @Override
    public Statement save(Statement statement) {
        return repository.save(statement);
    }
}

