package com.bank.accounts.statements.domain;

import java.time.YearMonth;

public record StatementPeriod(YearMonth yearMonth) {
    public static StatementPeriod of(int year, int month) {
        return new StatementPeriod(YearMonth.of(year, month));
    }
}

