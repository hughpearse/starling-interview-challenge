package com.starling.challenge.domain.services.starling;

import com.starling.challenge.config.MicroProfileConfig;
import com.starling.challenge.domain.model.starling.CreateOrUpdateSavingsGoalResponseV2;
import com.starling.challenge.domain.model.starling.CurrencyAndAmount;
import com.starling.challenge.domain.model.starling.SavingsGoalRequestV2;
import com.starling.challenge.domain.model.starling.SavingsGoalTransferResponseV2;
import com.starling.challenge.domain.model.starling.SavingsGoalV2;
import com.starling.challenge.domain.model.starling.SavingsGoalsV2;
import com.starling.challenge.domain.model.starling.TopUpRequestV2;
import com.starling.challenge.outboundclients.starling.BaseHttpClientImpl;
import com.starling.challenge.outboundclients.starling.SavingsGoalsClient;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

/**
 * Savings Goal Domain Service
 */
@ApplicationScoped
@Slf4j
public class SavingsGoalServiceImpl implements SavingsGoalService {

    private SavingsGoalsClient savingsGoalsClient;

    public SavingsGoalServiceImpl(MicroProfileConfig microProfileConfig) {
        this.savingsGoalsClient = new BaseHttpClientImpl<>(microProfileConfig.domain(), SavingsGoalsClient.class).getService();
    }

    public Uni<SavingsGoalV2> getSavingsGoal(UUID accountUid, String goalName) {
        log.info("Getting savings goal");
        Uni<SavingsGoalsV2> savingsGoals = savingsGoalsClient.getSavingsGoals(accountUid);
        Uni<List<SavingsGoalV2>> savingsGoalListUni = savingsGoals.onItem().transform(savingsGoalsV2 -> {
            List<SavingsGoalV2> savingsGoalList = savingsGoalsV2.getSavingsGoalList();
            return savingsGoalList;
        });
        return savingsGoalListUni.onItem().transformToMulti(Multi.createFrom()::iterable)
            .filter(savingsGoal -> goalName.equals(savingsGoal.getName()))
            .toUni()
            .onItem().ifNotNull().invoke(response -> log.info("Savings goal found."))
            .onFailure().invoke(t -> log.info("Savings goal not found."));
    }

    public Uni<CreateOrUpdateSavingsGoalResponseV2> createSavingsGoal(
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
        .onItem().ifNotNull().invoke(response -> log.info("Savings goal created."))
        .onFailure().invoke(t -> log.info("Savings goal not created."));
    }

    public Uni<SavingsGoalTransferResponseV2> transferToSavingsGoal(
        UUID accountUid,
        UUID savingsGoalUUID, 
        TopUpRequestV2 topUpRequestV2
    ) {
        log.info("Transferring to savings goal");
        UUID transferUid = UUID.randomUUID();
        return savingsGoalsClient.transferToSavingsGoal(accountUid, savingsGoalUUID, transferUid, topUpRequestV2)
        .onItem().ifNotNull().invoke(response -> log.info("Transfer completed."))
        .onFailure().invoke(t -> log.info("Transfer not completed."));
    }

    public Uni<UUID> getOrCreateSavingsGoal(
        UUID accountUid,
        String goalName,
        BigInteger optionalSavingsGoalTarget,
        Currency currency
    ){
        log.info("Investigating savings goal status");
        return getSavingsGoal(accountUid, goalName)
            .map(SavingsGoalV2::getSavingsGoalUid)
            .onItem().ifNull().switchTo(() -> {
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
                    .flatMap(item -> Uni.createFrom().item(item.getSavingsGoalUid()));
            });
    }
}
