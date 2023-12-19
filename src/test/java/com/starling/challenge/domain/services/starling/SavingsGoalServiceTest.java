package com.starling.challenge.domain.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;

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
    public void testGetSavingsGoal() {
        // Arrange: configure mock responses
        String goalName = "testGoal";
        AccountV2 account = new AccountV2();
        account.setAccountUid(UUID.randomUUID());
        account.setAccountType("Savings");
        account.setDefaultCategory("Personal");
        account.setCurrency("GBP");
        account.setCreatedAt("2023-12-19T12:30:28Z");
        account.setName("Test Account");
        CurrencyAndAmount target = new CurrencyAndAmount("GBP", BigInteger.valueOf(1000));
        CurrencyAndAmount totalSaved = new CurrencyAndAmount("GBP", BigInteger.valueOf(500));
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
        SavingsGoalV2 result = savingsGoalService.getSavingsGoal(account, goalName);

        // Assert: check results
        assertNotNull(result, "Savings goal should not be null");
        assertEquals(goalName, result.getName(), "Savings goal name should match");
    }
    
}
