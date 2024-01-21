package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.quarkus.test.InjectMock;

import java.math.BigInteger;
import java.net.URI;
import java.util.Arrays;
import java.util.Currency;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

@QuarkusTest
public class SavingsGoalServiceTest {
    
    @InjectMock
    private SavingsGoalsClient savingsGoalsClient;

    @InjectMock
    MicroProfileConfig microProfileConfig;

    @Test
    public void testGetSavingsGoal() throws Exception {
        // Arrange: configure mock responses
        String goalName = "testGoal";
        AccountV2 account = new AccountV2();
        account.setAccountUid(UUID.randomUUID());
        account.setAccountType(AccountV2.AccountType.PRIMARY);
        account.setDefaultCategory(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        account.setCurrency(Currency.getInstance("GBP"));
        account.setCreatedAt(DateUtils.parseDate("2023-01-01T00:00:00.000Z", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" }));
        account.setName("Test Account");
        CurrencyAndAmount target = new CurrencyAndAmount(Currency.getInstance("GBP"), BigInteger.valueOf(1000));
        CurrencyAndAmount totalSaved = new CurrencyAndAmount(Currency.getInstance("GBP"), BigInteger.valueOf(500));
        SavingsGoalV2 testGoal = new SavingsGoalV2(
            account.getAccountUid(),
            goalName,
            target,
            totalSaved,
            50,
            "ACTIVE");
        SavingsGoalsV2 savingsGoals = new SavingsGoalsV2(Arrays.asList(testGoal));
        when(savingsGoalsClient.getSavingsGoals(account.getAccountUid())).thenReturn(Uni.createFrom().item(savingsGoals));
        when(microProfileConfig.domain()).thenReturn(new URI("starling.test"));
        when(microProfileConfig.authorization()).thenReturn("token");
        when(microProfileConfig.useragent()).thenReturn("MyUserAgent");
        SavingsGoalServiceImpl savingsGoalService = new SavingsGoalServiceImpl(microProfileConfig);

        // Act: perform the test
        Uni<SavingsGoalV2> resultUni = savingsGoalService.getSavingsGoal(account.getAccountUid(), goalName);

        // Assert: check results
        resultUni.onItem().invoke(savingsGoal -> {
            assertNotNull(savingsGoal, "Savings goal should not be null");
            assertEquals(goalName, savingsGoal.getName(), "Savings goal name should match");
            assertEquals(account.getAccountUid(), savingsGoal.getSavingsGoalUid(), "Savings goal UID should match");
            assertEquals(target, savingsGoal.getTarget(), "Target amount should match");
            assertEquals(totalSaved, savingsGoal.getTotalSaved(), "Total saved amount should match");
            assertEquals(50, savingsGoal.getSavedPercentage(), "Saved percentage should match");
            assertEquals("ACTIVE", savingsGoal.getState(), "Savings goal state should match");
        }).await().indefinitely();
    }
    
}
