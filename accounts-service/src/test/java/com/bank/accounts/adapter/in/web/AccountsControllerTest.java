package com.bank.accounts.adapter.in.web;

import com.bank.accounts.history.application.port.in.GetTransactionHistoryUseCase;
import com.bank.accounts.lifecycle.application.port.in.GetAllAccountsUseCase;
import com.bank.accounts.lifecycle.application.port.out.LoadAccountPort;
import com.bank.accounts.lifecycle.domain.Account;
import com.bank.accounts.lifecycle.domain.AccountId;
import com.bank.accounts.lifecycle.domain.AccountStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountsController.class)
class AccountsControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GetAllAccountsUseCase getAllAccountsUseCase;
    
    @MockBean
    private GetTransactionHistoryUseCase getTransactionHistoryUseCase;
    
    @MockBean
    private LoadAccountPort loadAccountPort;
    
    @Test
    void getAccounts_shouldReturnListOfAccounts() throws Exception {
        AccountId accountId = AccountId.generate();
        Account account = Account.open(accountId, "customer-123", "CHECKING");
        
        when(getAllAccountsUseCase.getAllAccounts()).thenReturn(List.of(account));
        
        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].accountId").exists())
            .andExpect(jsonPath("$[0].customerId").value("customer-123"))
            .andExpect(jsonPath("$[0].productType").value("CHECKING"))
            .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }
    
    @Test
    void getTransactions_shouldReturnTransactionHistory() throws Exception {
        AccountId accountId = AccountId.generate();
        Account account = Account.open(accountId, "customer-123", "CHECKING");
        
        when(loadAccountPort.findById(any(AccountId.class))).thenReturn(Optional.of(account));
        when(getTransactionHistoryUseCase.getTransactionHistory(any(AccountId.class)))
            .thenReturn(List.of());
        
        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());
    }
    
    @Test
    void getTransactions_shouldReturn404WhenAccountNotFound() throws Exception {
        AccountId accountId = AccountId.generate();
        
        when(loadAccountPort.findById(any(AccountId.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/accounts/{accountId}/transactions", accountId.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}

