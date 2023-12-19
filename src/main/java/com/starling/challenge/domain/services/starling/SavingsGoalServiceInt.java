package com.starling.challenge.domain.services.starling;

import java.util.UUID;

import com.starling.challenge.domain.model.starling.AccountV2;
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
     * @param account the AccountV2 object
     * @param goalName the name of the goal; eg "Holidays"
     * @return a SavingsGoalV2 object
     */
    public SavingsGoalV2 getSavingsGoal(AccountV2 account, String goalName);

    /**
     * Create savings goal
     * @param account the AccountV2 object
     * @param savingsGoalRequestV2 the savingsGoalRequestV2 object
     * @return the CreateOrUpdateSavingsGoalResponseV2 object
     */
    public CreateOrUpdateSavingsGoalResponseV2 createSavingsGoal(AccountV2 account, SavingsGoalRequestV2 savingsGoalRequestV2);

    /**
     * Transfer to a savings goal
     * @param account the AccountV2 object
     * @param savingsGoalUUID savings goal UUID
     * @param topUpRequestV2 the TopUpRequestV2 object
     * @return a SavingsGoalTransferResponseV2 object
     */
    public SavingsGoalTransferResponseV2 transferToSavingsGoal(AccountV2 account, UUID savingsGoalUUID, TopUpRequestV2 topUpRequestV2);

     /**
      * Get or create savings goal if it doesnt exist.
      * @param account the AccountV2 object
      * @param goalName Savings goal name
      * @return Savings goal UUID
      */
    UUID getOrCreateSavingsGoal(AccountV2 account, String goalName);
}
