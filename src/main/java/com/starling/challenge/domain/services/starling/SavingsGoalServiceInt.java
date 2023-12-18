package com.starling.challenge.domain.services.starling;

import java.util.UUID;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;

/**
 * Savings Goals Domain Service
 */
public interface SavingsGoalServiceInt {

    /**
     * Get savings goal
     * @param goalName the name of the goal; eg "Holidays"
     * @param accountUid the account UUID
     * @return a SavingsGoalV2 object
     */
    public SavingsGoalV2 getSavingsGoal(String goalName, UUID accountUid);

    /**
     * Create savings goal
     * @param savingsGoalRequestV2 the savingsGoalRequestV2 object
     * @param accountUid the account UUID
     * @return the CreateOrUpdateSavingsGoalResponseV2 object
     */
    public CreateOrUpdateSavingsGoalResponseV2 createSavingsGoal(SavingsGoalRequestV2 savingsGoalRequestV2, UUID accountUid);

    /**
     * Transfer to a savings goal
     * @param accountUUID account UUID
     * @param savingsGoalUUID savings goal UUID
     * @param topUpRequestV2 the TopUpRequestV2 object
     * @return a SavingsGoalTransferResponseV2 object
     */
    public SavingsGoalTransferResponseV2 transferToSavingsGoal(UUID accountUUID, UUID savingsGoalUUID, TopUpRequestV2 topUpRequestV2);

    /**
     * Get or create savings goal if it doesnt exist.
     * @param accountUid account UUID
     * @param goalName Savings goal name
     * @return Savings goal UUID
     */
    UUID getOrCreateSavingsGoal(UUID accountUid, String goalName);
}
