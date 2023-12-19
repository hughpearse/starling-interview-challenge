package com.starling.challenge.outboundclients.services.starling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.services.starling.SavingsGoalService;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

@ExtendWith(MockitoExtension.class)
public class SavingsGoalServiceTest {
    private SavingsGoalsClient savingsGoalsClient = mock(SavingsGoalsClient.class);

    @Test
    public void testGetSavingsGoal() {
        // Arrange: configure mock responses
        String goalName = "testGoal";
        UUID accountUid = UUID.fromString("00000000-0000-0000-0000-000000000000");
        CurrencyAndAmount target = new CurrencyAndAmount("GBP", 1000);
        CurrencyAndAmount totalSaved = new CurrencyAndAmount("GBP", 500);
        SavingsGoalV2 testGoal = new SavingsGoalV2(
            accountUid,
            goalName,
            target,
            totalSaved,
            50,
            "ACTIVE");
        SavingsGoalsV2 savingsGoals = new SavingsGoalsV2(Arrays.asList(testGoal));
        when(savingsGoalsClient.getSavingsGoals(accountUid)).thenReturn(savingsGoals);
        SavingsGoalService savingsGoalService = new SavingsGoalService(savingsGoalsClient);

        // Act: perform the test
        SavingsGoalV2 result = savingsGoalService.getSavingsGoal(goalName, accountUid);

        // Assert: check results
        assertNotNull(result, "Savings goal should not be null");
        assertEquals(goalName, result.getName(), "Savings goal name should match");
    }
    
}
