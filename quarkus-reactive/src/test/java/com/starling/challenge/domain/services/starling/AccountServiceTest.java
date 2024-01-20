package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.quarkus.test.InjectMock;

import java.net.URI;
import java.util.Arrays;
import java.util.Currency;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.Accounts;
import com.starling.challenge.outboundclients.starling.AccountsClient;

@QuarkusTest
public class AccountServiceTest {

    @InjectMock
    AccountsClient accountsClient;

    @InjectMock
    MicroProfileConfig microProfileConfig;

    @Test
    public void testGetAccount() throws Exception {
        // Arrange: configure mock responses
        String accountName = "testAccount";
        AccountV2 testAccount = new AccountV2(
            UUID.fromString("00000000-0000-0000-0000-000000000000"), 
            AccountV2.AccountType.PRIMARY,
            UUID.fromString("00000000-0000-0000-0000-000000000001"), 
            Currency.getInstance("GBP"), 
            DateUtils.parseDate("2023-01-01T00:00:00.000Z", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" }), 
            "testAccount");
        Accounts accounts = new Accounts(Arrays.asList(testAccount));
        when(accountsClient.getAccounts()).thenReturn(Uni.createFrom().item(accounts));
        when(microProfileConfig.domain()).thenReturn(new URI("starling.test"));
        when(microProfileConfig.authorization()).thenReturn("token");
        when(microProfileConfig.useragent()).thenReturn("MyUserAgent");
        AccountsServiceImpl accountsService = new AccountsServiceImpl(microProfileConfig);

        // Act: perform the test
        Uni<AccountV2> resultUni = accountsService.getAccount(accountName);

        // Assert: check results
        resultUni.onItem().invoke(result -> {
            assertNotNull(result, "Account should not be null");
            assertEquals(accountName, result.getName(), "Account name should match");
        }).await().indefinitely();
    }

    
}
