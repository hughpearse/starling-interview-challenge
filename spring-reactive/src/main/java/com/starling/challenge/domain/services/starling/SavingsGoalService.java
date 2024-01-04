package com.starling.challenge.domain.services.starling;

import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

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
public class SavingsGoalService implements SavingsGoalServiceInt {

    private SavingsGoalsClient savingsGoalsClient;

    /**
     * Constructor for Savings Goal domain service.
     * @param savingsGoalsClient injected HTTP client for interacting with Savings goals.
     */
    public SavingsGoalService(
        SavingsGoalsClient savingsGoalsClient
    ){
        this.savingsGoalsClient = savingsGoalsClient;
    }

    public Mono<SavingsGoalV2> getSavingsGoal(UUID accountUid, String goalName) {
        Mono<SavingsGoalsV2> savingsGoals = savingsGoalsClient.getSavingsGoals(accountUid);
        Mono<List<SavingsGoalV2>> savingsGoalListMono = savingsGoals.map(savingsGoalsV2 -> {
            List<SavingsGoalV2> savingsGoalList = savingsGoalsV2.getSavingsGoalList();
            return savingsGoalList;
        });
        Mono<SavingsGoalV2> savingsGoalMono = savingsGoalListMono.flatMap(list -> {
            SavingsGoalV2 savingsGoal = list.stream().filter(savingsGoalV2 -> savingsGoalV2.getName().equals(goalName)).findFirst().orElse(null);
            return Mono.just(savingsGoal); 
        });
        savingsGoalMono = savingsGoalMono.map(account -> {
            if (account != null) {
                log.info("Savings goal found.");
            } else {
                log.info("No Savings goal found.");
            }
            return account;
        });
        return savingsGoalMono;
    }

    public Mono<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(
        UUID accountUid,
        SavingsGoalRequestV2 savingsGoalRequestV2
    ) {
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
            .doOnSuccess(response -> log.info("Savings goal created."));
    }    

    public Mono<SavingsGoalTransferResponseV2> transferToSavingsGoal(
        UUID accountUid,
        UUID savingsGoalUUID, 
        TopUpRequestV2 topUpRequestV2
    ) {
        UUID transferUid = UUID.randomUUID();
        return savingsGoalsClient.transferToSavingsGoal(accountUid, savingsGoalUUID, transferUid, topUpRequestV2)
            .doOnSuccess(response -> log.info("Transfer completed."));
    }
    

    public Mono<UUID> getOrCreateSavingsGoal(
        UUID accountUid,
        String goalName,
        BigInteger optionalSavingsGoalTarget,
        Currency currency
    ){
        return getSavingsGoal(accountUid, goalName)
            .map(SavingsGoalV2::getSavingsGoalUid)
            .switchIfEmpty(Mono.defer(() -> {
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
                    .map(CreateOrUpdateSavingsGoalResponseV2::getSavingsGoalUid);
            }));
    }    
    
}
