package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Currency;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.outboundclients.starling.AccountsClient;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    private AccountsClient accountsClient = mock(AccountsClient.class);

    @Test
    public void testGetAccount() {
        // Arrange: configure mock responses
        String accountName = "testAccount";
        AccountV2 testAccount = new AccountV2(
            UUID.fromString("00000000-0000-0000-0000-000000000000"), 
            "Current account",
            "shopping category", 
            Currency.getInstance("GBP"), 
            "2023-01-01", 
            "testAccount");
        Accounts accounts = new Accounts(Arrays.asList(testAccount));
        when(accountsClient.getAccounts()).thenReturn(Mono.just(accounts));
        AccountsService accountsService = new AccountsService(accountsClient);

        // Act: perform the test
        Mono<AccountV2> resultMono = accountsService.getAccount(accountName);

        // Assert: check results
        StepVerifier.create(resultMono)
            .assertNext(result -> {
                assertNotNull(result, "Account should not be null");
                assertEquals(accountName, result.getName(), "Account name should match");
            })
            .verifyComplete();
    }

    
}
