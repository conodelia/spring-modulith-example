package com.bank.accounts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI accountsServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Accounts Service API")
                .description("REST API for the Accounts bounded context in the retail banking system. " +
                    "This service manages account lifecycle, transactions, balances, holds, pricing, statements, and transaction history.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Banking Team")
                    .email("banking@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}

