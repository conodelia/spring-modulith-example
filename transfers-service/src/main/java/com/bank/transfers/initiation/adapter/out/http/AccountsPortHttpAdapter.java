package com.bank.transfers.initiation.adapter.out.http;

import com.bank.transfers.initiation.application.port.out.AccountsPort;
import com.bank.transfers.initiation.domain.AccountRef;
import com.bank.transfers.initiation.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * HTTP adapter for AccountsPort.
 * Communicates with accounts-service via REST API (no direct class dependencies).
 */
@Component
@RequiredArgsConstructor
public class AccountsPortHttpAdapter implements AccountsPort {

    private final RestTemplate restTemplate;

    @Value("${accounts.service.url:http://localhost:8080}")
    private String accountsServiceUrl;

    @Value("${accounts.service.api.base-path:/api}")
    private String apiBasePath;

    @Override
    public Optional<String> placeHold(AccountRef accountRef, Money amount, String reference) {
        try {
            // In a real implementation, this would call accounts-service REST API
            // For demo: POST /api/accounts/{accountId}/holds
            String url = accountsServiceUrl + apiBasePath + "/accounts/" + accountRef.getAccountId() + "/holds";
            // Placeholder - would make actual HTTP call
            return Optional.of("hold-ref-" + reference);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean releaseHold(String holdReference) {
        try {
            // In a real implementation: DELETE /api/accounts/holds/{holdReference}
            String url = accountsServiceUrl + apiBasePath + "/accounts/holds/" + holdReference;
            // Placeholder - would make actual HTTP call
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean postDebit(AccountRef accountRef, Money amount, String reference) {
        try {
            // In a real implementation: POST /api/accounts/{accountId}/postings (debit)
            String url = accountsServiceUrl + apiBasePath + "/accounts/" + accountRef.getAccountId() + "/postings";
            // Placeholder - would make actual HTTP call
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean postCredit(AccountRef accountRef, Money amount, String reference) {
        try {
            // In a real implementation: POST /api/accounts/{accountId}/postings (credit)
            String url = accountsServiceUrl + apiBasePath + "/accounts/" + accountRef.getAccountId() + "/postings";
            // Placeholder - would make actual HTTP call
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

