package com.starling.challenge.domain.services.starling;

import java.math.BigInteger;
import java.util.Currency;
import java.util.UUID;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;

import reactor.core.publisher.Mono;

/**
 * Savings Goals Domain Service
 */
public interface SavingsGoalService {

    /**
     * Get savings goal
     * @param accountUid the account UUID
     * @param goalName the name of the goal; eg "Holidays"
     * @return a SavingsGoalV2 object
     */
    public Mono<SavingsGoalV2> getSavingsGoal(UUID accountUid, String goalName);

    /**
     * Create savings goal
     * @param accountUid the account UUID
     * @param savingsGoalRequestV2 the savingsGoalRequestV2 object
     * @return the CreateOrUpdateSavingsGoalResponseV2 object
     */
    public Mono<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(UUID accountUid, SavingsGoalRequestV2 savingsGoalRequestV2);

    /**
     * Transfer to a savings goal
     * @param accountUid the account UUID
     * @param savingsGoalUUID savings goal UUID
     * @param topUpRequestV2 the TopUpRequestV2 object
     * @return a SavingsGoalTransferResponseV2 object
     */
    public Mono<SavingsGoalTransferResponseV2> transferToSavingsGoal(UUID accountUid, UUID savingsGoalUUID, TopUpRequestV2 topUpRequestV2);

    /**
     * 
     * @param accountUid the account UUID
     * @param goalName Savings goal name
     * @param optionalSavingsGoalTarget the goal target
     * @param currency the currency code
     * @return
     */
    Mono<UUID> getOrCreateSavingsGoal(UUID accountUid, String goalName, BigInteger optionalSavingsGoalTarget, Currency currency);
}
