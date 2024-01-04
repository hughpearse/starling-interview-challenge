package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Currency;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.starling.challenge.domain.model.starling.AccountV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

@ExtendWith(MockitoExtension.class)
public class SavingsGoalServiceTest {
    private SavingsGoalsClient savingsGoalsClient = mock(SavingsGoalsClient.class);

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
        when(savingsGoalsClient.getSavingsGoals(account.getAccountUid())).thenReturn(savingsGoals);
        SavingsGoalService savingsGoalService = new SavingsGoalService(savingsGoalsClient);

        // Act: perform the test
        SavingsGoalV2 result = savingsGoalService.getSavingsGoal(account.getAccountUid(), goalName);

        // Assert: check results
        assertNotNull(result, "Savings goal should not be null");
        assertEquals(goalName, result.getName(), "Savings goal name should match");
    }
    
}
