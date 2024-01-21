package com.starling.challenge.domain.services.starling;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Savings Goal Domain Service
 */
@Service
@Slf4j
@AllArgsConstructor
public class SavingsGoalServiceImpl implements SavingsGoalService {

    private SavingsGoalsClient savingsGoalsClient;

    public Mono<SavingsGoalV2> getSavingsGoal(UUID accountUid, String goalName) {
        log.info("Getting savings goal");
        Mono<SavingsGoalsV2> savingsGoals = savingsGoalsClient.getSavingsGoals(accountUid);
        Mono<List<SavingsGoalV2>> savingsGoalListMono = savingsGoals.map(savingsGoalsV2 -> {
            List<SavingsGoalV2> savingsGoalList = savingsGoalsV2.getSavingsGoalList();
            return savingsGoalList;
        });
        return savingsGoalListMono.flatMapIterable(savingsGoalsList -> savingsGoalsList)
        .filter(savingsGoal -> goalName.equals(savingsGoal.getName()))
        .next()
        .doOnSuccess(response -> log.info("Savings goal found."))
        .doOnError(t -> log.info("Savings goal not found."));
    }

    public Mono<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(
        UUID accountUid,
        SavingsGoalRequestV2 savingsGoalRequestV2
    ) {
        log.info("Creating new savings goal");
        CurrencyAndAmount request_CurrencyAndAmount = new CurrencyAndAmount(
            savingsGoalRequestV2.getCurrency(),
            savingsGoalRequestV2.getTarget().getMinorUnits()
        );
        SavingsGoalRequestV2 request = new SavingsGoalRequestV2(
            savingsGoalRequestV2.getName(), 
            savingsGoalRequestV2.getCurrency(),
            request_CurrencyAndAmount, 
            ""
        );
        return savingsGoalsClient.createSavingsGoal(accountUid, request)
            .doOnSuccess(response -> log.info("Savings goal created."))
            .doOnError(t -> log.info("Savings goal not created."));
    }    

    public Mono<SavingsGoalTransferResponseV2> transferToSavingsGoal(
        UUID accountUid,
        UUID savingsGoalUUID, 
        TopUpRequestV2 topUpRequestV2
    ) {
        log.info("Transferring to savings goal");
        UUID transferUid = UUID.randomUUID();
        return savingsGoalsClient.transferToSavingsGoal(accountUid, savingsGoalUUID, transferUid, topUpRequestV2)
            .doOnSuccess(response -> log.info("Transfer completed."))
            .doOnError(t -> log.info("Transfer not completed."));
    }

    public Mono<UUID> getOrCreateSavingsGoal(
        UUID accountUid,
        String goalName,
        BigInteger optionalSavingsGoalTarget,
        Currency currency
    ){
        log.info("Investigating savings goal status");
        return getSavingsGoal(accountUid, goalName)
            .map(savingsGoal -> savingsGoal.getSavingsGoalUid())
            .onErrorResume(e -> {
                CurrencyAndAmount request_CurrencyAndAmount = new CurrencyAndAmount(
                    currency, 
                    optionalSavingsGoalTarget
                );
                SavingsGoalRequestV2 savingsGoalRequestV2 = new SavingsGoalRequestV2(
                    goalName, 
                    currency, 
                    request_CurrencyAndAmount, 
                    ""
                );
                return createSavingsGoal(accountUid, savingsGoalRequestV2)
                    .map(newSavingsGoal -> newSavingsGoal.getSavingsGoalUid());
            });
    }
    
}
