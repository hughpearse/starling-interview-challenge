package com.starling.challenge.outboundclients.starling;

import java.util.UUID;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;

import reactor.core.publisher.Mono;

public interface SavingsGoalsClient {
    /**
     * Get savings goals.
     * @param accountUid account UUID
     * @return SavingsGoalsV2 object
     */
    public Mono<SavingsGoalsV2> getSavingsGoals(UUID accountUid);

    /**
     * Create or update savings goal
     * @param accountUid account UUID
     * @param request SavingsGoalRequestV2 object
     * @return CreateOrUpdateSavingsGoalResponseV2 object
     */
    public Mono<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(UUID accountUid, SavingsGoalRequestV2 savingsGoalRequestV2);

    /**
     * Transfer money to savings goal.
     * @param accountUid account UUID
     * @param savingsGoalUid savings goal UUID
     * @param transferUid client generated transfer UUID
     * @param request TopUpRequestV2 object
     * @return SavingsGoalTransferResponseV2 object
     */
    public Mono<SavingsGoalTransferResponseV2> transferToSavingsGoal(UUID accountUid, UUID savingsGoalUid, UUID transferUid, TopUpRequestV2 topUpRequestV2);
}
